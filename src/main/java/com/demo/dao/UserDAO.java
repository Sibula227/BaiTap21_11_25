package com.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import com.demo.entity.User;
import com.demo.utils.JPAUtils;

import java.util.List;

public class UserDAO {

    public User findById(int id) {
        EntityManager em = JPAUtils.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = JPAUtils.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u", User.class);
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void create(User user) {
        EntityManager em = JPAUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            // rollback an toàn và log lỗi
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e; // tùy ý: có thể bọc lại thành RuntimeException nếu muốn
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    public User findByUsername(String username) {
        EntityManager em = JPAUtils.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            q.setParameter("username", username);
            try {
                return q.getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void update(User user) {
        EntityManager em = JPAUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
