package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer " + "" +
                "(amount, transfer_status_id, transfer_type_id, account_from, account_to) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            int newTransferId = jdbcTemplate.queryForObject(sql,
                    int.class,
                    transfer.getAmount(),
                    transfer.getStatusId(),
                    transfer.getTypeId(),
                    transfer.getAccountFrom(),
                    transfer.getAccountTo()
//                    transfer.getUsernameFrom(),
//                    transfer.getUsernameTo(),
//                    transfer.getStatusDescription(),
//                    transfer.getTypeDescription()
            );

            return getTransferById(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer SET " +
                "amount = ?, transfer_status_id = ?, transfer_type_id = ?, account_from = ?, account_to = ? " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql,
                transfer.getAmount(),
                transfer.getStatusId(),
                transfer.getTypeId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getId()
                );

        return transfer;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        if (transferId <= 0)  {
            throw new IllegalArgumentException("Not Valid Transfer ID");
        }
        String sql = "SELECT transfer_id, amount, " +
                "user_from.username AS username_from, user_to.username AS username_to,  " +
                "account_from, account_to, " +
                "transfer_status_description, transfer_type_description, " +
                "transfer.transfer_status_id, transfer.transfer_type_id " +
                "FROM transfer " +
                "INNER JOIN account AS acc_from ON account_from = acc_from.account_id " +
                "INNER JOIN account AS acc_to ON account_to = acc_to.account_id " +
                "INNER JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id " +
                "INNER JOIN tenmo_user AS user_to ON acc_to.user_id = user_to.user_id " +
                "INNER JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "INNER JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "WHERE transfer_id = ? ;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        try{
        if (result.next()) {
            return mapRowToTransfer(result);
        }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }
    @Override
    public List<Transfer> getListOfTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, " +
                "user_from.username AS username_from, user_to.username AS username_to,  " +
                "account_from, account_to, " +
                "transfer_status_description, transfer_type_description, " +
                "transfer.transfer_status_id, transfer.transfer_type_id " +
                "FROM transfer " +
                "INNER JOIN account AS acc_from ON account_from = acc_from.account_id " +
                "INNER JOIN account AS acc_to ON account_to = acc_to.account_id " +
                "INNER JOIN tenmo_user AS user_from ON acc_from.user_id = user_from.user_id " +
                "INNER JOIN tenmo_user AS user_to ON acc_to.user_id = user_to.user_id " +
                "INNER JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "INNER JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "WHERE acc_from.user_id = ? OR acc_to.user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();
        transfer.setId(rs.getInt("transfer_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        transfer.setUsernameFrom(rs.getString("username_from"));
        transfer.setUsernameTo(rs.getString("username_to"));

        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));

        transfer.setStatusId(rs.getInt("transfer_status_id"));
        transfer.setTypeId(rs.getInt("transfer_type_id"));

        transfer.setStatusDescription(rs.getString("transfer_status_description"));
        transfer.setTypeDescription(rs.getString("transfer_type_description"));

        return transfer;
    }
}
