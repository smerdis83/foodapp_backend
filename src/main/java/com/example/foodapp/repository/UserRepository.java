package com.example.foodapp.repository;

import com.example.foodapp.config.HibernateUtil;
import com.example.foodapp.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository {

    /**
     * Save a new user or update an existing one
     */
    public User save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Find a user by its id
     */
    public Optional<User> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    /**
     * Find a user by username
     */
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery(
                    "FROM User u WHERE u.username = :uname", User.class);
            q.setParameter("uname", username);
            return q.uniqueResultOptional();
        }
    }

    /**
     * Find a user by email
     */
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery(
                    "FROM User u WHERE u.email = :mail", User.class);
            q.setParameter("mail", email);
            return q.uniqueResultOptional();
        }
    }

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
