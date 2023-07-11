
package com.techelevator.tenmo.controller;
import javax.validation.Valid;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;


    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id)  {
        try {
            return transferDao.getTransferById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(Principal principal) throws UsernameNotFoundException {
        int userId = getCurrentUserId(principal);
        try {
            return transferDao.getListOfTransfers(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Transfer create(@Valid @RequestBody Transfer transfer) {
        Transfer newTransfer = null;
        try {
            if (transfer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer was not created.");
            }

            newTransfer = transferDao.createTransfer(transfer);

            if (transfer.getStatusDescription().equals("Approved")){
                accountDao.updateAccountsByTransfer(newTransfer);
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer was not created.");
        }
        return newTransfer;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer update(@PathVariable int id, @Valid @RequestBody Transfer transfer) {
        transfer.setId(id);
        String status = transferDao.getTransferById(id).getStatusDescription();
        Transfer updatedTransfer = null;
        try {
            if (transfer.getAccountFrom() == transfer.getAccountTo()) {
                throw new DaoException("Cannot transfer to self"); // TODO: correct error message
            } else {
                updatedTransfer = transferDao.updateTransfer(transfer);
            }
            if (updatedTransfer.getStatusDescription().equals("Approved") &&
                    status.equals("Pending"))  {
                accountDao.updateAccountsByTransfer(updatedTransfer);
            }
            return updatedTransfer;
        } catch (DaoException e) {
            throw new DaoException ("Did not update transfer", e);
        }
    }
    /**
     * Finds the user by username and returns the id
     * @param principal the current authenticated user
     * @return int the id of the user
     */
    private int getCurrentUserId(Principal principal) {
        return userDao.getUserByUsername(principal.getName()).getId();
    }
}
