package domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Reservations")
public class Reservation implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;
    @Id
    @Column
    private LocalDate date;

    public Reservation() {
    }

    public Reservation(User user, ParkingLot parkingLot, LocalDate date) {
        this.user = user;
        this.parkingLot = parkingLot;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return user + "-" + parkingLot;
    }
}
