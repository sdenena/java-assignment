package org.sd.services.implementations;

import org.sd.entity.Account;
import org.sd.entity.User;
import org.sd.repositories.AccountRepository;
import org.sd.services.AccountService;
import org.sd.util.Generator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AccountServiceImpl implements AccountService {
    private final Scanner scanner = new Scanner(System.in);
    AccountRepository accountRepository = new AccountRepository();

    @Override
    public Account createAccount() {
        Account account = new Account();

        System.out.print("User id: ");
        account.setUser(new User(scanner.nextLong()));
        scanner.nextLine();

        System.out.print("Input Account type: ");
        String input = scanner.nextLine().trim().toUpperCase();

        try {
            Account.AccountType accountType = Account.AccountType.valueOf(input);
            account.setAccountType(accountType);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid account type entered.");
        }

        account.setAccountNumber(Generator.generateAccountNumber(accountRepository.getMaxId() + 1));

        return accountRepository.saveAccount(account);
    }

    @Override
    public Account updateAccount() {
        return null;
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> getAccountList(int page, int size) {
        return List.of();
    }

    @Override
    public List<Account> getAllAccount() {
        return List.of();
    }
}
