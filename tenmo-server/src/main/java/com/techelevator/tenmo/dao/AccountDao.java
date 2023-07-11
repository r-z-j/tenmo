package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccountById(int id);
    Account getAccountByUserId(int id);
    BigDecimal getBalanceByAccountId(int accountId);
    BigDecimal getBalanceByUserId(int userId);
    void updateAccountsByTransfer(Transfer transfer);
}
