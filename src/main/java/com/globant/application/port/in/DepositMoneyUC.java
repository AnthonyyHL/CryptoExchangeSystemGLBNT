package com.globant.application.port.in;

import java.math.BigDecimal;

public interface DepositMoneyUC {
    void depositFiat(BigDecimal amount);
}
