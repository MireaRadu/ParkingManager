package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table( name = "Sites" )
public class Site implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private long id;
    private String name;
    @OneToMany(mappedBy="site")
    private Collection<ParkingLot> parkingLots;

    public Site() {
    }

    public Site(String name, Collection<ParkingLot> parkingLots) {
        this.name = name;
        this.parkingLots = parkingLots;
    }
}
