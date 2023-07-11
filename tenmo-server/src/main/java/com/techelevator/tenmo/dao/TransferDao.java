package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int id);
    List<Transfer> getListOfTransfers(int id);
    Transfer createTransfer(Transfer transfer);
    Transfer updateTransfer(Transfer transfer);
}
