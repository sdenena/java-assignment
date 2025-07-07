package org.sd.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.sd.configurations.HibernateUtil;
import org.sd.entity.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    // Create User
    public User saveUser(User user) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("User saved successfully: " + user);
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            System.err.println("Error save user: " + e.getMessage());
            throw new RuntimeException("Error save user: " + e.getMessage());
        }
    }

    // Get By ID
    public Optional<User> findUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.of(user);
        } catch (Exception e) {
            System.err.println("Error finding user by id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to find user", e);
        }
    }


    // Get User List
    public List<User> getAllUsers(int page, int size) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User u ORDER BY u.id DESC", User.class
            );
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            throw new RuntimeException("Error getting all users: " + e.getMessage());
        }
    }

    // Get All User
    public List<User> getAllUsers() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User u ORDER BY u.id DESC", User.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            throw new RuntimeException("Error getting all users: " + e.getMessage());
        }
    }

    // Update User
    public User updateUser(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User updateUser = session.merge(user);
            transaction.rollback();
            System.out.println("User update successfully: " + user);
            return updateUser;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error update user: " + e.getMessage());
            throw new RuntimeException("Error update user: ", e);
        }
    }

    // Delete User
    public boolean deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                System.out.println("User deleted successfully: " + id);
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting user with id: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
