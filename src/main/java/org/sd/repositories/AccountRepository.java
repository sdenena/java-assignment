package org.sd.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.sd.configurations.HibernateUtil;
import org.sd.entity.Account;

import java.util.List;
import java.util.Optional;

public class AccountRepository {
    // Create User
    public Account saveAccount(Account account) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
            System.out.println("Account saved successfully: " + account);
            return account;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            System.err.println("Error save account: " + e.getMessage());
            throw new RuntimeException("Error save account: " + e.getMessage());
        }
    }

    // Get By ID
    public Optional<Account> findAccountById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account account = session.get(Account.class, id);
            return Optional.of(account);
        } catch (Exception e) {
            System.err.println("Error finding account by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to find account", e);
        }
    }

    // Get Max Id
    public Long getMaxId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT MAX(a.id) FROM Account a", Long.class
            );
            Long maxId = query.uniqueResult();
            return (maxId != null) ? maxId : 0L;
        } catch (Exception e) {
            System.err.println("Error getting max account ID: " + e.getMessage());
            throw new RuntimeException("Error getting max account ID", e);
        }
    }

    // Get Account List
    public List<Account> getAllAccount(int page, int size) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                    "FROM Account u ORDER BY u.id DESC", Account.class
            );
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            throw new RuntimeException("Error getting all users: " + e.getMessage());
        }
    }

    // Get All Account
    public List<Account> getAllAccount() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                    "FROM Account u ORDER BY u.id DESC", Account.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all account: " + e.getMessage());
            throw new RuntimeException("Error getting all account: " + e.getMessage());
        }
    }

    // Update Account
    public Account updateAccount(Account account) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Account updatedAccount = session.merge(account);
            transaction.commit(); // ✅ Commit to save changes
            System.out.println("Account updated successfully: " + updatedAccount);
            return updatedAccount;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating account: " + e.getMessage());
            throw new RuntimeException("Error updating account: ", e);
        }
    }

    // Get Account By Account Number
    public Optional<Account> findByAccountNumber(String accountNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> query = session.createQuery(
                    "FROM Account acc WHERE acc.accountNumber = :accNumber", Account.class);
            query.setParameter("accNumber", accountNumber);
            Account account = query.uniqueResult();
            return Optional.ofNullable(account);
        } catch (Exception e) {
            System.err.println("Error finding user by accountNumber: " + accountNumber + " - " + e.getMessage());
            throw new RuntimeException("Failed to find user by email", e);
        }
    }
}
