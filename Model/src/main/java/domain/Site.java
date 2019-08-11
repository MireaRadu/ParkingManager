package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "Sites")
public class Site implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "site_id")
    private Collection<ParkingLotPositioned> parkingLots;

    public Site() {
    }

    public Site(String name) {
        this.name = name;
        this.parkingLots = new ArrayList<ParkingLotPositioned>();
    }

    public Site(String name, Collection<ParkingLotPositioned> parkingLots) {
        this.name = name;
        this.parkingLots = parkingLots;
    }

    public Collection<ParkingLotPositioned> getParkingLots() {
        return parkingLots;
    }

    public String getName() {
        return name;
    }

    public void addParkingLot(ParkingLotPositioned parkingLot) {
        parkingLots.add(parkingLot);
    }

    public void removeParkingLot(ParkingLotPositioned parkingLot) {
        parkingLots.remove(parkingLot);
    }

    @Override
    public String toString() {
        return name;
    }
}
