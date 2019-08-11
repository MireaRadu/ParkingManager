import domain.*;
import repository.RepositoryReservationInterface;
import repository.RepositorySite;
import repository.RepositoryUserInterface;
import services.Observer;
import services.ServerInterface;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Server implements ServerInterface {

    private RepositoryUserInterface repositoryUser;
    private RepositoryReservationInterface repositoryReservation;
    private RepositorySite repositorySite;
    private Set<Observer> observers = new HashSet<>();

    public Server(RepositoryUserInterface repositoryUser, RepositoryReservationInterface repositoryReservation, RepositorySite repositorySite) {
        this.repositoryUser = repositoryUser;
        this.repositoryReservation = repositoryReservation;
        this.repositorySite = repositorySite;
    }

    @Override
    public Collection<Site> getSites() throws RemoteException {
        return repositorySite.getSites();
    }

    @Override
    public User login(String username, String password, Observer observer) throws RemoteException {
        User user = repositoryUser.getUser(username, password);
        if (user != null) {
            observers.add(observer);
        }
        return user;
    }

    @Override
    public void logout(Observer observer) throws RemoteException {
        removeObserver(observer);
    }

    @Override
    public void removeReservationForDate(User user, ParkingLot parkingLot, LocalDate start, LocalDate end, int step) throws RemoteException {
        while (start.isBefore(end) || start.isEqual(end)) {
            repositoryReservation.removeReservationForDate(start, user, parkingLot);
            start = start.plusDays(step);
        }
        notifyObservers();
    }

    @Override
    public void makeReservations(User user, ParkingLot parkingLot, LocalDate start, LocalDate end, int step) throws RemoteException {
        while (start.isBefore(end) || start.isEqual(end)) {
            Reservation reservation = new Reservation(user, parkingLot, start);
            repositoryReservation.addReservation(reservation);
            start = start.plusDays(step);
        }
        notifyObservers();
    }

    @Override
    public Collection<Reservation> getReservationsForDate(LocalDate localDate) throws RemoteException {
        return repositoryReservation.getReservationForDate(localDate);
    }

    @Override
    public Collection<Reservation> getReservationsForUser(User user) throws RemoteException {
        return repositoryReservation.getReservationForUser(user);
    }

    @Override
    public void addUser(User user) {
        repositoryUser.addUser(user);
        notifyObservers();
    }

    @Override
    public void removeUser(User user) {
        for (Reservation reservation : repositoryReservation.getReservationForUser(user)) {
            repositoryReservation.removeReservation(reservation);
        }
        repositoryUser.removeUser(user);
        notifyObservers();
    }

    @Override
    public void addSite(Site site) {
        repositorySite.addSite(site);
        notifyObservers();
    }

    @Override
    public void removeSite(Site site) {
        repositorySite.removeSite(site);
        notifyObservers();
    }

    @Override
    public void addParkingLotToSite(Site site, ParkingLotPositioned parkingLot) {
        repositorySite.addParkingLotToSite(site, parkingLot);
        notifyObservers();
    }

    @Override
    public void removeParkingLotFromSite(Site site, ParkingLotPositioned parkingLot) {
        for (Reservation reservation : repositoryReservation.getReservationForParkingLot(parkingLot.getParkingLot())) {
            repositoryReservation.removeReservation(reservation);
        }
        repositorySite.removeParkingLotFromSite(site, parkingLot);
        notifyObservers();
    }

    @Override
    public Collection<User> getUsers() throws RemoteException {
        return repositoryUser.getUsers();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) throws RemoteException {
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(x -> {
            try {
                x.update();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

}
