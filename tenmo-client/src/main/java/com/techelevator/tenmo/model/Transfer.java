package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int accountTo;
    private int accountFrom;
    private int statusId;
    private int typeId;
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

    public Transfer(){};

    @Override
    public String toString() {
        return "Transfer id = " + id +
                "\n Amount = $" + amount +
                "\n From = " + usernameFrom +
                "\n To = " + usernameTo +
                "\n Status = " + statusDescription +
                "\n Type = " + typeDescription + " \n";
    }

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
}
