package com.globant.application.port.in;

import com.globant.domain.entities.User;
import com.globant.domain.entities.currencies.Currency;

import java.math.BigDecimal;

public interface BuyFromExchangeUC {
    void buyCurrency(int option, BigDecimal amount);
    void updateUserWallet(User activeUser, Currency currency, BigDecimal amount);
}
