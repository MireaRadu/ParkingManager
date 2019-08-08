package domain;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "Reservations" )
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne
    @JoinColumn(name="parking_lot_id", nullable=false)
    private ParkingLot parkingLot;
    @Temporal(TemporalType.TIMESTAMP)
    private DateTime date;

    public Reservation() {
    }

    public Reservation(User user, ParkingLot parkingLot, DateTime date) {
        this.user = user;
        this.parkingLot = parkingLot;
        this.date = date;
    }
}
