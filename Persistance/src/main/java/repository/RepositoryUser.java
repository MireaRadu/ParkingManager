package repository;

import domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;

public class RepositoryUser implements RepositoryUserInterface {
    public User getUser(String name, String password) {
        User user = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                user = session.createQuery("from User where userName = :name and password = :password", User.class)
                        .setParameter("name", name)
                        .setParameter("password", password)
                        .list()
                        .get(0);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return user;
    }

    public void addUser(User user) {
        JdbcUtils.addOrUpdateToDatabase(user);
    }

    @Override
    public void removeUser(User user) {
        JdbcUtils.removeFromDatabase(user);
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> users = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                users = session.createQuery("from User", User.class)
                        .list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return users;
    }
}
