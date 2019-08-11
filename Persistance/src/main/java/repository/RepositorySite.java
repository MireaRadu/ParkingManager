package repository;

import domain.ParkingLotPositioned;
import domain.Site;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;

public class RepositorySite implements RepositorySiteInterface {
    @Override
    public Collection<Site> getSites() {
        Collection<Site> sites = null;
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                sites = session.createQuery("from Site", Site.class)
                        .list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

        return sites;
    }

    @Override
    public void addSite(Site site) {
        JdbcUtils.addOrUpdateToDatabase(site);
    }

    @Override
    public void removeSite(Site site) {
        for (ParkingLotPositioned parkingLotPositioned : site.getParkingLots()) {
            JdbcUtils.removeFromDatabase(parkingLotPositioned.getParkingLot());
            JdbcUtils.removeFromDatabase(parkingLotPositioned);
        }
        JdbcUtils.removeFromDatabase(site);
    }

    @Override
    public void addParkingLotToSite(Site site, ParkingLotPositioned parkingLot) {
        site.addParkingLot(parkingLot);
        JdbcUtils.addOrUpdateToDatabase(site);
    }

    @Override
    public void removeParkingLotFromSite(Site site, ParkingLotPositioned parkingLot) {
        JdbcUtils.removeFromDatabase(parkingLot);
        JdbcUtils.removeFromDatabase(parkingLot.getParkingLot());
        site.removeParkingLot(parkingLot);
        JdbcUtils.addOrUpdateToDatabase(site);
    }
}
