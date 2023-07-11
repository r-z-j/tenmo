package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService;
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        accountService = new AccountService(API_BASE_URL, currentUser);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance();
        System.out.println("Current Balance: \n" + "$" +balance);

    }

    private void viewTransferHistory() {
        Transfer[] transferHistory = accountService.getListOfTransfers();
        if (transferHistory.length == 0) {
            System.out.println("No transfers have been created on this account");
        } else {

            System.out.println("Transfer History:");
            for (Transfer transfer : transferHistory) {
                System.out.println(transfer.toString());
            }
        }
    }

    private void viewPendingRequests() {
        Transfer[] transferHistory = accountService.getListOfTransfers();
        if (transferHistory.length == 0) {
            System.out.println("No transfers have been created on this account");
        } else {
            System.out.println("Pending Transfers: ");
            for (Transfer transfer : transferHistory) {
                if (transfer.getStatusDescription().equals("Pending")) {
                    System.out.println(transfer.toString());
                }
            }
        }
        int selection = consoleService.promptForInt("Select Transfer by Id: \nO: Exit \n");
        Transfer transferToUpdate = null;
        for (Transfer transfer : transferHistory) {
            if (transfer.getId() == selection) {
                transferToUpdate = transfer;
            }
        }
        if (transferToUpdate == null && selection != 0) {
            System.out.println("Invalid input");
        } else if(selection != 0) {
            int newStatus = consoleService.promptForInt("Update Status: \n0: Reject \n1: Approve \n");
            int currentUserAccount = accountService.getAccountIdByUserId(currentUser.getUser().getId()).getId();
            if (currentUserAccount != transferToUpdate.getAccountTo()) {
                System.out.println("Not authorized to update transfer");
            } else {
                if (newStatus == 0){
                    transferToUpdate.setStatusId(4003);
                    transferToUpdate.setStatusDescription("Rejected");
                    accountService.putTransfer(transferToUpdate);
                    System.out.println(transferToUpdate.toString());
                } else if (newStatus == 1) {
                    transferToUpdate.setStatusId(4002);
                    transferToUpdate.setStatusDescription("Approved");
                    accountService.putTransfer(transferToUpdate);
                    System.out.println(transferToUpdate.toString());
                }
            }
            }

    }

    private void sendBucks() {
        User[] userList = accountService.getListOfUsers();
        System.out.println("List of Users: ");
        for (int i = 0; i < userList.length; i++){
            System.out.println(i + ". " + userList[i].getUsername());
        }
        int selection = consoleService.promptForInt("Select a user to send a transfer: \n");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter an Amount: \n");
        int userId = userList[selection].getId();
        Account accountTo = accountService.getAccountIdByUserId(userId);
        Account accountFrom = accountService.getAccountIdByUserId(currentUser.getUser().getId());
        Transfer transfer = new Transfer();
        transfer.setAccountTo(accountTo.getId());
        transfer.setAccountFrom(accountFrom.getId());
        transfer.setUsernameTo(userList[selection].getUsername());
        transfer.setUsernameFrom(currentUser.getUser().getUsername());
        transfer.setAmount(amount);
        transfer.setStatusDescription("Approved");
        transfer.setTypeDescription("Send");
        transfer.setStatusId(4002);
        transfer.setTypeId(3002);

        System.out.println(accountService.postTransfer(transfer));
}

    private void requestBucks() {
        User[] userList = accountService.getListOfUsers();
        System.out.println("List of Users: ");
        for (int i = 0; i < userList.length; i++){
            System.out.println(i + ". " + userList[i].getUsername());
        }
        int selection = consoleService.promptForInt("Select a user to request a transfer: \n");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter an Amount: \n");
        int userId = userList[selection].getId();
        Account accountTo = accountService.getAccountIdByUserId(userId);
        Account accountFrom = accountService.getAccountIdByUserId(currentUser.getUser().getId());
        Transfer transfer = new Transfer();
        transfer.setAccountTo(accountTo.getId());
        transfer.setAccountFrom(accountFrom.getId());
        transfer.setUsernameTo(userList[selection].getUsername());
        transfer.setUsernameFrom(currentUser.getUser().getUsername());
        transfer.setAmount(amount);
        transfer.setStatusDescription("Pending");
        transfer.setTypeDescription("Request");
        transfer.setStatusId(4001);
        transfer.setTypeId(3001);

        System.out.println(accountService.postTransfer(transfer));
    }

}
