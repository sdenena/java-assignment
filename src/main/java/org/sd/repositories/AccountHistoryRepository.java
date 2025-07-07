package org.sd.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.sd.configurations.HibernateUtil;
import org.sd.entity.Account;
import org.sd.entity.AccountHistory;

import java.util.List;

public class AccountHistoryRepository {
    // Create Account History
    public AccountHistory saveHistory(AccountHistory accountHistory) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.persist(accountHistory);

            transaction.commit();
            System.out.println("Account History saved successfully: " + accountHistory);
            return accountHistory;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error saving account: " + e.getMessage());
            throw new RuntimeException("Error saving account: " + e.getMessage());

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Get All Account
    public List<AccountHistory> getAllHistory() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AccountHistory> query = session.createQuery(
                    "SELECT ah FROM AccountHistory ah", AccountHistory.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error getting all account: " + e.getMessage());
            throw new RuntimeException("Error getting all account: " + e.getMessage());
        }
    }
}
