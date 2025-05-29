package com.example.foodapp.repository;

import com.example.foodapp.config.HibernateUtil;
import com.example.foodapp.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository {

    /**
     * Save a new user or update an existing one.
     */
    public User save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception rbEx) {
                    rbEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Find a user by ID.
     */
    public Optional<User> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    /**
     * Find a user by phone number.
     */
    public Optional<User> findByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery(
                "FROM User u WHERE u.phone = :phone", User.class);
            q.setParameter("phone", phone);
            return q.uniqueResultOptional();
        }
    }

    /**
     * Check if a phone number is already registered.
     */
    public boolean existsByPhone(String phone) {
        return findByPhone(phone).isPresent();
    }

    /**
     * Find a user by email.
     */
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery(
                "FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            return q.uniqueResultOptional();
        }
    }

    /**
     * Check if an email is already registered.
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
