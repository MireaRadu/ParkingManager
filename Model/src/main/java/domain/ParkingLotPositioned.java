package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ParkingLotsPositioned")
public class ParkingLotPositioned implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parkingLot_id")
    private ParkingLot parkingLot;
    private int xPosition;
    private int yPosition;

    public ParkingLotPositioned() {
    }

    public ParkingLotPositioned(ParkingLot parkingLot, int xPosition, int yPosition) {
        this.parkingLot = parkingLot;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    @Override
    public String toString() {
        return parkingLot.getName();
    }
}
