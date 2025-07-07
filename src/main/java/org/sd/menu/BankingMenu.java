package org.sd.menu;

import org.sd.entity.Account;
import org.sd.entity.AccountHistory;
import org.sd.entity.User;
import org.sd.services.*;
import org.sd.services.implementations.*;
import org.sd.util.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankingMenu {
    private static final String ANSI_CLEAR_SCREEN = "\033[2J\033[H";

    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final DepositService depositService = new DepositServiceImpl();
    private final WithdrawService withdrawService = new WithdrawServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();

    public void start() {
        System.out.println("Welcome to Banking System!");
        System.out.println("Use Enter to select, 'q' to quit");
        System.out.println("Press any key to continue...");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showMainMenu();
    }

    private void showMainMenu() {
        List<String> options = new ArrayList<>();
        options.add("User Information");
        options.add("Account Information");
        options.add("Deposit");
        options.add("Withdraw");
        options.add("Transfer");

        int choice = showMenu("=== MAIN MENU ===", options);

        switch (choice) {
            case 0:
                showUserInformationMenu();
                break;
            case 1:
                showAccountInformationMenu();
                break;
            case 2:
                handleDeposit();
                break;
            case 3:
                handleWithdraw();
                break;
            case 4:
                handleTransfer();
                break;
            default:
                showMainMenu();
        }
    }

    private void showUserInformationMenu() {
        List<String> options = new ArrayList<>();
        options.add("Create User");
        options.add("View User");
        options.add("← Back to Main Menu");

        int choice = showMenu("=== USER INFORMATION ===", options);

        switch (choice) {
            case 0:
                handleCreateUser();
                break;
            case 1:
                showViewUserMenu();
                break;
            case 2:
                showMainMenu();
                break;
            default:
                showUserInformationMenu();
        }
    }

    private void showViewUserMenu() {
        System.out.println("=== USER LIST ===");
        List<User> userList = userService.getAllUsers();

        // Display users in table format
        Table.displayUserTable(userList);

        List<String> options = new ArrayList<>();
        options.add("Edit User");
        options.add("Delete User");
        options.add("← Back to User Information");

        int choice = showMenu("=== VIEW USER ===", options);

        switch (choice) {
            case 0:
                handleEditUser();
                break;
            case 1:
                handleDeleteUser();
                break;
            case 2:
                showUserInformationMenu();
                break;
            default:
                showViewUserMenu();
        }
    }

    private void showAccountInformationMenu() {
        List<String> options = new ArrayList<>();
        options.add("Create Account");
        options.add("View Account");
        options.add("View Account History");
        options.add("← Back to Main Menu");

        int choice = showMenu("=== ACCOUNT INFORMATION ===", options);

        switch (choice) {
            case 0:
                handleCreateAccount();
                break;
            case 1:
                handleViewAccount();
                break;
            case 2:
                handleViewAccountHistory();
                break;
            case 3:
                showMainMenu();
                break;
            default:
                showAccountInformationMenu();
        }
    }

    private int showMenu(String title, List<String> options) {
        int selectedIndex = 0;

        while (true) {
            // Clear screen
            System.out.print(ANSI_CLEAR_SCREEN);

            // Display title
            System.out.println(title);
            System.out.println("=".repeat(title.length()));
            System.out.println();

            // Display options
            for (int i = 0; i < options.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + options.get(i));
            }

            System.out.println();
            System.out.println("Controls: Enter = select, 'q' = quit");
            System.out.print("Your choice: ");

            // Get user input
            try {
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.isEmpty() || input.equals("enter")) {
                    // Enter key (empty line)
                    return selectedIndex;
                } else if (input.equals("q") || input.equals("quit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else if (input.matches("\\d+")) {
                    // Number selection
                    int numChoice = Integer.parseInt(input) - 1;
                    if (numChoice >= 0 && numChoice < options.size()) {
                        return numChoice;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    // Invalid input
                    System.out.println("Invalid input. Use 'w' (up), 's' (down), Enter (select), or 'q' (quit)");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCreateUser() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== CREATE USER ===");
        System.out.println("Creating new user...");

        // Create User
        User user = userService.createUser();

        System.out.println("User created: " + user);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showUserInformationMenu();
    }

    private void handleEditUser() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== EDIT USER ===");
        System.out.println("Editing user...");

        User user = userService.updateUser();

        System.out.println("User was update successfully: " + user);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showViewUserMenu();
    }

    private void handleDeleteUser() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== DELETE USER ===");
        System.out.println("Deleting user...");

        boolean deleteUser = userService.deleteUser();

        if (deleteUser) {
            System.out.println("User was deleted successfully!");
        }

        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showViewUserMenu();
    }

    private void handleCreateAccount() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== CREATE ACCOUNT ===");
        System.out.println("Creating new account...");

        // Crate Account
        Account account = accountService.createAccount();

        System.out.println("Account was created successfully: " + account);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showAccountInformationMenu();
    }

    private void handleViewAccount() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== VIEW ACCOUNT ===");
        List<Account> accountList = accountService.getAllAccount();

        // Display users in table format
        Table.displayUserTable(accountList);

        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showAccountInformationMenu();
    }

    private void handleDeposit() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== DEPOSIT ===");
        System.out.println("Processing deposit...");

        AccountHistory accountHistory = depositService.deposit();
        System.out.println("Deposited account: " + accountHistory);
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showMainMenu();
    }

    private void handleWithdraw() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== WITHDRAW ===");
        System.out.println("Processing withdrawal...");

        AccountHistory accountHistory = withdrawService.withdraw();
        System.out.println("Withdraw from account: " + accountHistory);

        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showMainMenu();
    }

    private void handleTransfer() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== TRANSFER ===");
        System.out.println("Processing transfer...");

        AccountHistory accountHistory = transferService.transfer();
        System.out.println("Withdraw from account: " + accountHistory);

        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showMainMenu();
    }

    private void handleViewAccountHistory() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.println("=== VIEW ACCOUNT HISTORY ===");

        List<AccountHistory> accountHistoryList = accountService.getAllAccountHistory();

        // Display users in table format
        Table.displayUserTable(accountHistoryList);

        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        showMainMenu();
    }
}
