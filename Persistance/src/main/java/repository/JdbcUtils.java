package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Collection;

public class JdbcUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null || sessionFactory.isClosed()) {

            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
            }

        }

        return sessionFactory;

    }

    public static <T> void addOrUpdateToDatabase(T element) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                session.saveOrUpdate(element);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public static <T> void addToDatabase(T element) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                session.save(element);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public static <T> Collection<T> getElementsFromDatabase(String query, Class<T> tClass) {
        Collection<T> elements = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                elements = session.createQuery(query, tClass).list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return elements;
    }

    public static <T> void removeFromDatabase(T element) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();
                session.remove(element);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

}
