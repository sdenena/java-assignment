package org.sd.services.implementations;

import org.sd.entity.Account;
import org.sd.entity.AccountHistory;
import org.sd.entity.User;
import org.sd.repositories.AccountHistoryRepository;
import org.sd.repositories.AccountRepository;
import org.sd.services.AccountService;
import org.sd.services.DepositService;

import java.util.Optional;
import java.util.Scanner;

public class DepositServiceImpl implements DepositService {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountHistoryRepository accountHistoryRepository = new AccountHistoryRepository();
    private final AccountService accountService = new AccountServiceImpl();
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public AccountHistory deposit() {
        AccountHistory accountHistory = new AccountHistory();

        AccountHistory.TransactionType transactionType = AccountHistory.TransactionType.DEPOSIT;
        accountHistory.setTransactionType(transactionType);

        System.out.print("Input account number: ");
        accountHistory.setToAccountNumber(scanner.nextLine());

        System.out.print("Input amount: ");
        accountHistory.setAmount(scanner.nextBigDecimal());

        // Check if user exists
        Optional<Account> existingAccount = accountService.getAccountByAccountNumber(accountHistory.getToAccountNumber().trim());
        if (existingAccount.isEmpty()) {
            throw new RuntimeException("Account not found with accountNumber: " + accountHistory.getToAccountNumber().trim());
        }

        Account accountToUpdate = existingAccount.get();
        accountToUpdate.setBalance(accountToUpdate.getBalance().add(accountHistory.getAmount()));

        accountRepository.updateAccount(accountToUpdate);

        return accountHistoryRepository.saveHistory(accountHistory);
    }
}
