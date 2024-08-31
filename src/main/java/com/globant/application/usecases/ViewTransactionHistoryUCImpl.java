package com.globant.application.usecases;

import com.globant.application.port.in.ViewTransactionHistoryUC;
import com.globant.domain.entities.Transaction;
import com.globant.domain.repositories.ActiveUser;

import java.util.List;

public class ViewTransactionHistoryUCImpl implements ViewTransactionHistoryUC {
    @Override
    public List<Transaction> getTransactionHistory() {
        return ActiveUser.getInstance().getActiveUser().getTransactions();
    }
}
