package repository;

import domain.ParkingLot;
import domain.Reservation;
import domain.User;
import enums.UserType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Collection;

public class RepositoryReservation implements RepositoryReservationInterface {
    @Override
    public void addReservation(Reservation reservation) {
        JdbcUtils.addToDatabase(reservation);
    }

    @Override
    public void removeReservation(Reservation reservation) {
        JdbcUtils.removeFromDatabase(reservation);
    }

    @Override
    public void removeReservationForDate(LocalDate localDate, User user, ParkingLot parkingLot) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();
                Query query;
                if (!user.getUserType().equals(UserType.Admin)) {
                    query = session.createQuery("delete Reservation where (date = :date" +
                            " and user = :user" +
                            " and parkingLot = :parkingLot)");
                    query.setParameter("date", localDate)
                            .setParameter("user", user)
                            .setParameter("parkingLot", parkingLot);
                } else {
                    query = session.createQuery("delete Reservation where date = :date" +
                            " and parkingLot = :parkingLot");
                    query.setParameter("date", localDate)
                            .setParameter("parkingLot", parkingLot);
                }

                int result = query.executeUpdate();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

    }

    @Override
    public Collection<Reservation> getReservationForDate(LocalDate localDate) {
        Collection<Reservation> reservations = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                reservations = session.createQuery("from Reservation where date = :date", Reservation.class)
                        .setParameter("date", localDate)
                        .list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return reservations;
    }

    @Override
    public Collection<Reservation> getReservationForUser(User user) {
        Collection<Reservation> reservations = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                reservations = session.createQuery("from Reservation where user = :user", Reservation.class)
                        .setParameter("user", user)
                        .list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return reservations;
    }

    @Override
    public Collection<Reservation> getReservationForParkingLot(ParkingLot parkingLot) {
        Collection<Reservation> reservations = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                reservations = session.createQuery("from Reservation where parkingLot = :parkingLot", Reservation.class)
                        .setParameter("parkingLot", parkingLot)
                        .list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return reservations;
    }
}
