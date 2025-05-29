package com.example.foodapp.repository;

import com.example.foodapp.config.HibernateUtil;
import com.example.foodapp.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class UserRepository {

    /**
     * Save a new user or update an existing one
     */
    public User save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);   // store the user object
            tx.commit();             // apply changes to the database
            return user;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();       // undo changes if something goes wrong
            }
            throw e;
        }
    }

    /**
     * Find a user by its id
     */
    public Optional<User> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);  // load user from database
            return Optional.ofNullable(user);
        }
    }
}
