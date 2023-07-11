package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if (result.next()) {
            return result.getBigDecimal("balance");
        }
        return null;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            return result.getBigDecimal("balance");
        }
        return null;
    }


    public void updateAccountsByTransfer(Transfer transfer) {
        int accountFromId = transfer.getAccountFrom();
        int accountToId = transfer.getAccountTo();
        BigDecimal balanceFrom = getBalanceByAccountId(accountFromId);
        BigDecimal balanceTo = getBalanceByAccountId(accountToId);
        BigDecimal amount = transfer.getAmount();
        String type = transfer.getTypeDescription();

        BigDecimal newBalanceFrom;
        BigDecimal newBalanceTo;

        String sql = "";

        try {
            if (accountFromId == accountToId) {
                throw new DaoException("Cannot transfer to self"); // TODO: error message functionality
            }
            if (type.equals("Send")) { // Send money accountFrom -> accountTo
                if (balanceFrom.compareTo(amount) < 0) {
                    throw new DaoException("Transfer amount is greater than the account balance");
                } else {
                    newBalanceFrom = balanceFrom.subtract(amount);
                    sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
                    jdbcTemplate.update(sql, newBalanceFrom, accountFromId);

                    newBalanceTo = balanceTo.add(amount);
                    jdbcTemplate.update(sql, newBalanceTo, accountToId);
                }
            }
            if (type.equals("Request")) { // Send money accountTo -> accountFrom
                if (balanceTo.compareTo(amount) < 0) {
                    throw new DaoException("Transfer amount is greater than the account balance");
                } else {
                    newBalanceFrom = balanceTo.subtract(amount);
                    sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
                    jdbcTemplate.update(sql, newBalanceFrom, accountToId);

                    newBalanceTo = balanceFrom.add(amount);
                    jdbcTemplate.update(sql, newBalanceTo, accountFromId);
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
