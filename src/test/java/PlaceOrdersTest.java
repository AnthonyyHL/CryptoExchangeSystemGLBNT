import com.globant.application.config.UsersLoader;
import com.globant.application.port.out.UserRepository;
import com.globant.application.usecases.InitializeCurrencyPricesUCImpl;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.orders.Order;
import com.globant.domain.repositories.*;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlaceOrdersTest {
    static UserRepository userRepository = new UserManager();
    static UsersLoader usersLoader = new UsersLoader(userRepository);
    OrderBook orderBook = new OrderBook();
    private void testPlacerBuyOrder() {
        usersLoader.loadUsers();
        ActiveUser.getInstance().setActiveUser(userRepository.getByUsername("anthleon"));
        Wallet wallet1 = userRepository.getByUsername("anthleon").getWallet();
        Wallet wallet2 = userRepository.getByUsername("jose123").getWallet();
        Exchange exchange = new Exchange();

        InitializeCurrencyPricesUCImpl initializeCurrencyPricesUC = new InitializeCurrencyPricesUCImpl(exchange);
        initializeCurrencyPricesUC.loadCurrencies(); //Cargar instancias de monedas disponibles en todo el sistema

        Currency.setReferenceCurrency(Currency.getInstance("USD"));

        Currency btc = Currency.getInstance("BTC");
        Currency eth = Currency.getInstance("ETH");

        wallet1.deposit(Currency.getInstance("USD"), new BigDecimal(40000));
        wallet1.deposit(Currency.getInstance("EUR"), new BigDecimal(160000));
        wallet1.addCryptocurrency(btc, new BigDecimal(20));
        wallet1.addCryptocurrency(eth, new BigDecimal(20));

        wallet2.deposit(Currency.getInstance("USD"), new BigDecimal(40000));
        wallet2.deposit(Currency.getInstance("EUR"), new BigDecimal(160000));
        wallet2.addCryptocurrency(btc, new BigDecimal(20));
        wallet2.addCryptocurrency(eth, new BigDecimal(20));

        Order buyOrder = orderBook.createOrder(TradeType.BUY, btc, new BigDecimal(1), new BigDecimal(49000));
        Order buyOrder3 = orderBook.createOrder(TradeType.BUY, eth, new BigDecimal(1), new BigDecimal(2000));
        Order sellOrder = orderBook.createOrder(TradeType.SELL, btc, new BigDecimal(2), new BigDecimal(51000));
        Order sellOrder3 = orderBook.createOrder(TradeType.SELL, eth, new BigDecimal(1), new BigDecimal(1900));

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(buyOrder3);
        orderBook.addOrder(sellOrder);
        orderBook.addOrder(sellOrder3);

        ActiveUser.getInstance().logOutActiveUser();
        ActiveUser.getInstance().setActiveUser(userRepository.getByUsername("jose123"));
        Order buyOrder2 = orderBook.createOrder(TradeType.BUY, btc, new BigDecimal(2), new BigDecimal(53000));
        Order buyOrder4 = orderBook.createOrder(TradeType.BUY, eth, new BigDecimal(2), new BigDecimal(1900));
        Order sellOrder2 = orderBook.createOrder(TradeType.SELL, btc, new BigDecimal(1), new BigDecimal(48000));
        Order sellOrder4 = orderBook.createOrder(TradeType.SELL, eth, new BigDecimal(2), new BigDecimal(1800));

        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(buyOrder4);
        orderBook.addOrder(sellOrder2);
        orderBook.addOrder(sellOrder4);

        Map<Currency, TreeMap<BigDecimal, List<Order>>> orderBuyList = orderBook.getBuyOrders();
        Map<Currency, TreeMap<BigDecimal, List<Order>>> orderSellList = orderBook.getSellOrders();

        orderBuyList.forEach((currency, orders) -> {
            orders.forEach((price, orderList) -> {
                orderList.forEach(order -> {
                    System.out.printf("BUY ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n", order.getOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount());
                });
            });
        });
        System.out.println();
        orderSellList.forEach((currency, orders) -> {
            orders.forEach((price, orderList) -> {
                orderList.forEach(order -> {
                    System.out.printf("SELL ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n", order.getOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount());
                });
            });
        });

        System.out.println("Anthleon: " + userRepository.getByUsername("anthleon").getWallet().getBalance());
        System.out.println("Anthleon wallet: " + userRepository.getByUsername("anthleon").getWallet().getFiats());
        System.out.println("Jose123: " + userRepository.getByUsername("jose123").getWallet().getBalance());
        System.out.println("Jose123 wallet: " + userRepository.getByUsername("jose123").getWallet().getFiats());

        System.out.println();
        orderBook.matchSeller(buyOrder);

        System.out.println();
        System.out.println("Anthleon: " + userRepository.getByUsername("anthleon").getWallet().getBalance());
        System.out.println("Anthleon wallet: " + userRepository.getByUsername("anthleon").getWallet().getFiats());
        System.out.println("Jose123: " + userRepository.getByUsername("jose123").getWallet().getBalance());
        System.out.println("Jose123 wallet: " + userRepository.getByUsername("jose123").getWallet().getFiats());
        System.out.println();

        orderBook.matchBuyer(sellOrder);
        System.out.println();

        System.out.println("Anthleon: " + userRepository.getByUsername("anthleon").getWallet().getBalance());
        System.out.println("Anthleon wallet: " + userRepository.getByUsername("anthleon").getWallet().getFiats());
        System.out.println("Jose123: " + userRepository.getByUsername("jose123").getWallet().getBalance());
        System.out.println("Jose123 wallet: " + userRepository.getByUsername("jose123").getWallet().getFiats());
        System.out.println();

        Map<Currency, TreeMap<BigDecimal, List<Order>>> newOrderBuyList = orderBook.getBuyOrders();
        Map<Currency, TreeMap<BigDecimal, List<Order>>> newOrderSellList = orderBook.getSellOrders();

        newOrderBuyList.forEach((currency, orders) -> {
            orders.forEach((price, orderList) -> {
                orderList.forEach(order -> {
                    System.out.printf("BUY ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n", order.getOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount());
                });
            });
        });
        System.out.println();
        newOrderSellList.forEach((currency, orders) -> {
            orders.forEach((price, orderList) -> {
                orderList.forEach(order ->
                        System.out.printf("SELL ORDER #%s = User: %s | Currency: %s | Price: %s | Amount: %s\n", order.getOrderId(), order.getOrderEmitter().getUsername(), currency.getName(), price, order.getAmount()));
            });
        });
    }

    public static void main(String[] args) {
        PlaceOrdersTest test = new PlaceOrdersTest();
        test.testPlacerBuyOrder();
    }
}
