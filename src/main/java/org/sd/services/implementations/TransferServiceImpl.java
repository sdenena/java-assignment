package org.sd.services.implementations;

import org.sd.entity.Account;
import org.sd.entity.AccountHistory;
import org.sd.repositories.AccountHistoryRepository;
import org.sd.repositories.AccountRepository;
import org.sd.services.AccountService;
import org.sd.services.TransferService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class TransferServiceImpl implements TransferService {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountHistoryRepository accountHistoryRepository = new AccountHistoryRepository();
    private final AccountService accountService = new AccountServiceImpl();
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public AccountHistory transfer() {
        AccountHistory accountHistory = new AccountHistory();

        accountHistory.setTransactionType(AccountHistory.TransactionType.TRANSFER);


        System.out.print("Input transfer account number: ");
        String inputFromAccountNumber = scanner.nextLine();
        System.out.print("Input to account number: ");
        String inputToAccountNumber = scanner.nextLine();

        System.out.print("Input amount: ");
        accountHistory.setAmount(scanner.nextBigDecimal());
        accountHistory.setFromAccountNumber(inputFromAccountNumber);
        accountHistory.setToAccountNumber(inputToAccountNumber);

        // Check if user exists
        Optional<Account> existingFromAccount = accountService.getAccountByAccountNumber(accountHistory.getFromAccountNumber().trim());
        if (existingFromAccount.isEmpty()) {
            throw new RuntimeException("Account not found with accountNumber: " + accountHistory.getFromAccountNumber().trim());
        }

        Optional<Account> existingToAccount = accountService.getAccountByAccountNumber(accountHistory.getToAccountNumber().trim());
        if (existingToAccount.isEmpty()) {
            throw new RuntimeException("Account not found with accountNumber: " + accountHistory.getToAccountNumber().trim());
        }

        if (existingFromAccount.get().getBalance().subtract(accountHistory.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        Account fromAccountToUpdate = existingFromAccount.get();
        fromAccountToUpdate.setBalance(
                fromAccountToUpdate.getBalance().subtract(accountHistory.getAmount())
        );

        Account toAccountToUpdate = existingToAccount.get();
        toAccountToUpdate.setBalance(
                toAccountToUpdate.getBalance().subtract(accountHistory.getAmount())
        );

        accountRepository.updateAccount(fromAccountToUpdate);

        return accountHistoryRepository.saveHistory(accountHistory);
    }
}
