package com.globant.domain.repositories;

import com.globant.application.port.out.OrderBookRepository;
import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.entities.orders.BuyOrder;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.entities.orders.SellOrder;
import com.globant.domain.util.InvalidOrderException;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.*;

public class OrderBook implements OrderBookRepository {
    private Map<Currency, TreeMap<BigDecimal, List<Order>>> buyOrders;
    private Map<Currency, TreeMap<BigDecimal, List<Order>>> sellOrders;
    private User activeUser;

    public OrderBook() {
        buyOrders = new HashMap<>();
        sellOrders = new HashMap<>();
    }

    @Override
    public Order createOrder(TradeType tradeType, Currency cryptoType, BigDecimal amount, BigDecimal price) {
        activeUser = ActiveUser.getInstance().getActiveUser();
        WalletRepository userWallet = activeUser.getWallet();
        if (tradeType.equals(TradeType.BUY)) {
            if (userWallet.getBalance().compareTo(price.multiply(amount)) < 0) {
                throw new InvalidOrderException("Insufficient funds");
            }
            return new BuyOrder(cryptoType, amount, price);
        } else {
            Map<Crypto, BigDecimal> userWalletCryptocurrencies = userWallet.getCryptocurrencies();
            if (userWalletCryptocurrencies.get(cryptoType) == null || userWalletCryptocurrencies.get(cryptoType).compareTo(amount) < 0) {
                throw new InvalidOrderException("Insufficient cryptocurrency");
            }
            return new SellOrder(cryptoType, amount, price);
        }
    }
    @Override
    public void addOrder(Order order) {
        Crypto crypto = order.getCryptoType();
        if (order instanceof BuyOrder) {
            TreeMap<BigDecimal, List<Order>> newOrder = buyOrders.computeIfAbsent(crypto, k -> new TreeMap<>());
            List<Order> orders = newOrder.computeIfAbsent(((BuyOrder) order).getMaximumPrice(), k -> new ArrayList<>());
            orders.add(order);

        } else {
            TreeMap<BigDecimal, List<Order>> newOrder = sellOrders.computeIfAbsent(crypto, k -> new TreeMap<>());
            List<Order> orders = newOrder.computeIfAbsent(((SellOrder) order).getMinimumPrice(), k -> new ArrayList<>());
            orders.add(order);
        }
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getBuyOrdersByUsername(String username) {
        Map<Currency, TreeMap<BigDecimal, List<Order>>> buyOrdersByUsername = new HashMap<>();

        buyOrders.forEach((crypto, orders) ->
            orders.forEach((price, orderList) -> {
                List<Order> userOrders = orderList.stream()
                        .filter(order -> order.getOrderEmitter()
                        .getUsername().equals(username)).toList();
                buyOrdersByUsername.put(crypto, new TreeMap<>(Map.of(price, userOrders)));
        }));

        return buyOrdersByUsername;
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrdersByUsername(String username) {
        Map<Currency, TreeMap<BigDecimal, List<Order>>> sellOrdersByUsername = new HashMap<>();

        sellOrders.forEach((crypto, orders) ->
                orders.forEach((price, orderList) -> {
                    List<Order> userOrders = orderList.stream()
                            .filter(order -> order.getOrderEmitter()
                                    .getUsername().equals(username)).toList();
                    sellOrdersByUsername.put(crypto, new TreeMap<>(Map.of(price, userOrders)));
                }));

        return sellOrdersByUsername;
    }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getBuyOrders() { return buyOrders; }
    @Override
    public Map<Currency, TreeMap<BigDecimal, List<Order>>> getSellOrders() { return sellOrders; }
    @Override
    public void matchSeller(Order buyOrder) {
        Currency crypto = buyOrder.getCryptoType();
        TreeMap<BigDecimal, List<Order>> sellers = sellOrders.get(crypto);

        if (buyOrders == null || sellers == null) {
            addOrder(buyOrder);
            return;
        }

        BigDecimal price = ((BuyOrder) buyOrder).getMaximumPrice();
        while (price != null) {
            List<Order> orders = sellers.get(price);
            if (orders != null && !orders.isEmpty()) {
                Order seller = orders.get(0);
                if (!seller.getOrderEmitter().equals(buyOrder.getOrderEmitter()) && seller.getAmount().compareTo(buyOrder.getAmount()) == 0) {
                    match(seller, buyOrder);

                    orders.remove(0);
                    if (orders.isEmpty()) {
                        sellers.remove(price);
                        if (sellers.isEmpty()) sellOrders.remove(crypto);
                    }
                    return;
                }
            }
            price = sellers.lowerKey(price);
        }
    }

    @Override
    public void matchBuyer(Order sellOrder) {
        Currency crypto = sellOrder.getCryptoType();
        TreeMap<BigDecimal, List<Order>> buyers = buyOrders.get(crypto);

        if (buyers == null || sellOrders == null) {
            addOrder(sellOrder);
            return;
        }

        BigDecimal price = ((SellOrder) sellOrder).getMinimumPrice();
        while (price != null) {
            List<Order> orders = buyers.get(price);
            if (orders != null && !orders.isEmpty()) {
                Order buyer = orders.get(0);
                if (!buyer.getOrderEmitter().equals(sellOrder.getOrderEmitter()) && buyer.getAmount().compareTo(sellOrder.getAmount()) == 0) {
                    match(sellOrder, buyer);

                    orders.remove(0);
                    if (orders.isEmpty()) {
                        buyers.remove(price);
                        if (buyers.isEmpty()) buyOrders.remove(crypto);
                    }

                    return;
                }
            }
            price = buyers.higherKey(price);
        }
    }

    @Override
    public void match(Order sellOrder, Order buyOrder) {
        User seller = sellOrder.getOrderEmitter();
        User buyer = buyOrder.getOrderEmitter();

        WalletRepository sellerWallet = seller.getWallet();
        WalletRepository buyerWallet = buyer.getWallet();

        Currency currency = sellOrder.getCryptoType();
        BigDecimal price = ((SellOrder) sellOrder).getMinimumPrice();
        BigDecimal amount = sellOrder.getAmount();
        BigDecimal total = price.multiply(amount);

        Currency referenceCurrency = Currency.getReferenceCurrency();

        Transaction sellerTransaction = seller.getWallet().makeOrderTransaction(currency, amount, price, TradeType.SELL, "SELL ORDER");
        seller.addTransaction(sellerTransaction);

        Transaction buyerTransaction = buyer.getWallet().makeOrderTransaction(currency, amount, price, TradeType.BUY, "BUY ORDER");
        buyer.addTransaction(buyerTransaction);

        sellerWallet.deposit(referenceCurrency, total);
        sellerWallet.addCryptocurrency(currency, amount.negate());

        Map<Fiat, BigDecimal> buyerFiatCurrencies = buyerWallet.getFiats();
        BigDecimal[] remainingAmount = {total};

        if (buyerFiatCurrencies.containsKey(referenceCurrency)) {
            BigDecimal fiatAmount = buyerFiatCurrencies.get(referenceCurrency);
            if (fiatAmount.compareTo(remainingAmount[0]) < 0) {
                buyerWallet.deposit(referenceCurrency, fiatAmount.negate());
                remainingAmount[0] = remainingAmount[0].subtract(fiatAmount);
            } else {
                buyerWallet.deposit(referenceCurrency, remainingAmount[0].negate());
                buyerWallet.addCryptocurrency(currency, amount);
                return;
            }
        }

        if (remainingAmount[0].compareTo(BigDecimal.ZERO) > 0) {
            List<Fiat> fiatCurrencies = new ArrayList<>(buyerFiatCurrencies.keySet());

            for (Fiat fiat : fiatCurrencies) {
                BigDecimal fiatAmount = buyerFiatCurrencies.get(fiat);
                if (remainingAmount[0].compareTo(BigDecimal.ZERO) > 0 && !fiat.equals(referenceCurrency)) {
                    BigDecimal equivalentAmount = remainingAmount[0].divide(fiat.getPrice(), 2, BigDecimal.ROUND_HALF_UP);
                    if (fiatAmount.compareTo(equivalentAmount) < 0) {
                        remainingAmount[0] = remainingAmount[0].subtract(fiatAmount.multiply(fiat.getPrice()));
                        buyerWallet.deposit(fiat, BigDecimal.ZERO);
                    } else {
                        buyerWallet.deposit(fiat, equivalentAmount.negate());
                        remainingAmount[0] = BigDecimal.ZERO;
                    }
                }
            }
        }
        buyerWallet.addCryptocurrency(currency, amount);
    }
}
