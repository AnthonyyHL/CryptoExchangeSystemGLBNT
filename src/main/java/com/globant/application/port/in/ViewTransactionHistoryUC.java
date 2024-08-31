package com.globant.application.port.in;

import com.globant.domain.entities.Transaction;

import java.util.List;

public interface ViewTransactionHistoryUC {
    List<Transaction> getTransactionHistory();
}
