package org.sd.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sd.configurations.HibernateUtil;
import org.sd.entity.AccountHistory;

public class AccountHistoryRepository {
    // Create Account History
    public AccountHistory deposit(AccountHistory accountHistory) {
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
}
