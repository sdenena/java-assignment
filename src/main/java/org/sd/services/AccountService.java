package org.sd.services;

import org.sd.entity.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    public Account createAccount();
    public Account updateAccount();
    public Optional<Account> getAccountById(Long id);
    public Optional<Account> getAccountByAccountNumber(String accountNumber);
    public List<Account> getAccountList(int page, int size);
    public List<Account> getAllAccount();
}
