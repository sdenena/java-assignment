package org.sd.services.implementations;

import org.sd.entity.Account;
import org.sd.entity.AccountHistory;
import org.sd.repositories.AccountHistoryRepository;
import org.sd.repositories.AccountRepository;
import org.sd.services.AccountService;
import org.sd.services.WithdrawService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class WithdrawServiceImpl implements WithdrawService {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountHistoryRepository accountHistoryRepository = new AccountHistoryRepository();
    private final AccountService accountService = new AccountServiceImpl();
    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public AccountHistory withdraw() {
        AccountHistory accountHistory = new AccountHistory();

        AccountHistory.TransactionType transactionType = AccountHistory.TransactionType.WITHDRAW;
        accountHistory.setTransactionType(transactionType);

        System.out.print("Input account number: ");
        accountHistory.setFromAccountNumber(scanner.nextLine());

        System.out.print("Input amount: ");
        accountHistory.setAmount(scanner.nextBigDecimal());

        // Check if user exists
        Optional<Account> existingAccount = accountService.getAccountByAccountNumber(accountHistory.getFromAccountNumber().trim());
        if (existingAccount.isEmpty()) {
            throw new RuntimeException("Account not found with accountNumber: " + accountHistory.getFromAccountNumber().trim());
        }

        if (existingAccount.get().getBalance().subtract(accountHistory.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        Account accountToUpdate = existingAccount.get();
        accountToUpdate.setBalance(
                accountToUpdate.getBalance().subtract(accountHistory.getAmount())
        );

        accountRepository.updateAccount(accountToUpdate);

        return accountHistoryRepository.saveHistory(accountHistory);
    }
}
