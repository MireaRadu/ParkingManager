package repository;

import domain.Reservation;
import org.joda.time.DateTime;

import java.util.Collection;

public interface RepositoryReservationInterface {
    void addReservation(Reservation reservation);
    void removeReservationForDate(DateTime dateTime);
    Collection<Reservation> getReservationForDate(DateTime dateTime);
}
