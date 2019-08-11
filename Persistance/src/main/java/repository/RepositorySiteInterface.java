package repository;

import domain.ParkingLotPositioned;
import domain.Site;

import java.util.Collection;

interface RepositorySiteInterface {
    Collection<Site> getSites();

    void addSite(Site site);

    void removeSite(Site site);

    void addParkingLotToSite(Site site, ParkingLotPositioned parkingLot);

    void removeParkingLotFromSite(Site site, ParkingLotPositioned parkingLot);
}
