package repository;

import domain.ParkingLot;
import domain.Reservation;
import domain.User;

import java.time.LocalDate;
import java.util.Collection;

public interface RepositoryReservationInterface {
    void addReservation(Reservation reservation);

    void removeReservation(Reservation reservation);

    void removeReservationForDate(LocalDate localDate, User user, ParkingLot parkingLot);

    Collection<Reservation> getReservationForDate(LocalDate localDate);

    Collection<Reservation> getReservationForUser(User user);

    Collection<Reservation> getReservationForParkingLot(ParkingLot parkingLot);
}
