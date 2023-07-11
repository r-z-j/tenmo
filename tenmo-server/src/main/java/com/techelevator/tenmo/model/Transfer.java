package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Objects;

/*
Details for transfer:
- Transfer ID
- Name of  user from
- Name of user to
- Type of transfer (request money / send money)
- Status of transfer (pending / approved / rejected)
- Amount of Transfer
*/

public class Transfer {
    private int id;
    private int accountTo;
    private int accountFrom;
    private int statusId;
    private int typeId;
    @DecimalMin(value = "0.001", message = "Must be greater than zero")
    private BigDecimal amount;

    private String usernameFrom;
    private String usernameTo;
    private String statusDescription;
    private String typeDescription;

    public Transfer(int id, int accountTo, int accountFrom, int statusId, int typeId,
                    BigDecimal amount, String usernameFrom, String usernameTo,
                    String statusDescription, String typeDescription) {
        this.id = id;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.statusId = statusId;
        this.typeId = typeId;
        this.amount = amount;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.statusDescription = statusDescription;
        this.typeDescription = typeDescription;
    }
    public Transfer(int id, int accountTo, int accountFrom, int statusId, int typeId, BigDecimal amount) {
        this.id = id;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.statusId = statusId;
        this.typeId = typeId;
        this.amount = amount;

    }
    public Transfer(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", accountTo=" + accountTo +
                ", accountFrom=" + accountFrom +
                ", statusId=" + statusId +
                ", typeId=" + typeId +
                ", amount=" + amount +
                ", usernameFrom='" + usernameFrom + '\'' +
                ", usernameTo='" + usernameTo + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return id == transfer.id && accountTo == transfer.accountTo && accountFrom == transfer.accountFrom && statusId == transfer.statusId && typeId == transfer.typeId && amount.equals(transfer.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountTo, accountFrom, statusId, typeId, amount);
    }
}
