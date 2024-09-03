package com.globant.domain.repositories;

import com.globant.application.port.out.WalletRepository;
import com.globant.domain.entities.currencies.Crypto;
import com.globant.domain.entities.Transaction;
import com.globant.domain.entities.currencies.Currency;
import com.globant.domain.entities.currencies.Fiat;
import com.globant.domain.util.TradeType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet implements WalletRepository {
    private Map<Crypto, BigDecimal> cryptocurrencies;
    private Map<Fiat, BigDecimal> fiats;
    private BigDecimal balance;

    public Wallet() {
        this.cryptocurrencies = new HashMap<>();
        this.fiats = new HashMap<>();
        this.balance = new BigDecimal(0);
    }

    @Override
    public BigDecimal getBalance() { return balance; }

    @Override
    public Map<Fiat, BigDecimal> getFiats() {
        return fiats;
    }

    @Override
    public Map<Crypto, BigDecimal> getCryptocurrencies() {
        return cryptocurrencies;
    }

    @Override
    public Transaction makeTransaction(Currency currency, BigDecimal amount, TradeType tradeType, String source) {
        return new Transaction((Crypto) currency, amount, tradeType, source);
    }
    @Override
    public Transaction makeOrderTransaction(Currency currency, BigDecimal amount, BigDecimal priceAtTheMoment, TradeType tradeType, String source) {
        return new Transaction((Crypto) currency, amount, priceAtTheMoment, tradeType, source);
    }
    @Override
    public void updateBalance(Fiat fiat, BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public void addCryptocurrency(Currency currency, BigDecimal amount) {
        Crypto crypto = (Crypto) currency;

        BigDecimal total = crypto.getPrice().multiply(amount);
        BigDecimal[] remainingAmount = {total};
        Fiat referenceCurrency = (Fiat) Currency.getReferenceCurrency();

        if (cryptocurrencies.isEmpty() || !cryptocurrencies.containsKey(crypto))
            cryptocurrencies.put(crypto, BigDecimal.ZERO);

        if (fiats.containsKey(referenceCurrency)) {
            BigDecimal fiatAmount = fiats.get(referenceCurrency);
            if (fiatAmount.compareTo(remainingAmount[0]) < 0) {
                deposit(referenceCurrency, fiatAmount.negate());
                remainingAmount[0] = remainingAmount[0].subtract(fiatAmount);
            } else {
                deposit(referenceCurrency, remainingAmount[0].negate());
                cryptocurrencies.put(crypto, cryptocurrencies.get(crypto).add(amount));
                return;
            }
        }

        if (remainingAmount[0].compareTo(BigDecimal.ZERO) > 0) {
            List<Fiat> fiatCurrencies = new ArrayList<>(fiats.keySet());

            for (Fiat fiat : fiatCurrencies) {
                BigDecimal fiatAmount = fiats.get(fiat);
                if (remainingAmount[0].compareTo(BigDecimal.ZERO) > 0 && !fiat.equals(referenceCurrency)) {
                    BigDecimal equivalentAmount = remainingAmount[0].divide(fiat.getPrice(), 2, BigDecimal.ROUND_HALF_UP);
                    if (fiatAmount.compareTo(equivalentAmount) < 0) {
                        remainingAmount[0] = remainingAmount[0].subtract(fiatAmount.multiply(fiat.getPrice()));
                        deposit(fiat, BigDecimal.ZERO);
                    } else {
                        deposit(fiat, equivalentAmount.negate());
                        remainingAmount[0] = BigDecimal.ZERO;
                    }
                }
            }
        }
        cryptocurrencies.put(crypto, cryptocurrencies.get(crypto).add(amount));
    }
    @Override
    public void deposit(Currency currency, BigDecimal amount) {
        Fiat fiat = (Fiat) currency;

        if (fiats.isEmpty() || !fiats.containsKey(fiat))
            fiats.put(fiat, BigDecimal.ZERO);
        fiats.put(fiat, fiats.get(fiat).add(amount));

        BigDecimal amountEquivalence = fiat.getExchangeCurrencyRate();
        updateBalance(fiat, amount.multiply(amountEquivalence).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}