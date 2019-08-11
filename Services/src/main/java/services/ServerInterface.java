package services;

import domain.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Collection;

public interface ServerInterface extends Remote {

    Collection<Site> getSites() throws RemoteException;

    User login(String username, String password, Observer observer) throws RemoteException;

    void logout(Observer observer) throws RemoteException;

    void removeReservationForDate(User user, ParkingLot parkingLot, LocalDate start, LocalDate end, int step) throws RemoteException;

    void makeReservations(User user, ParkingLot parkingLot, LocalDate start, LocalDate end, int step) throws RemoteException;

    Collection<Reservation> getReservationsForDate(LocalDate localDate) throws RemoteException;

    Collection<Reservation> getReservationsForUser(User user) throws RemoteException;

    void addUser(User user) throws RemoteException;

    void removeUser(User user) throws RemoteException;

    void addSite(Site site) throws RemoteException;

    void removeSite(Site site) throws RemoteException;

    void addParkingLotToSite(Site site, ParkingLotPositioned parkingLot) throws RemoteException;

    void removeParkingLotFromSite(Site site, ParkingLotPositioned parkingLot) throws RemoteException;

    Collection<User> getUsers() throws RemoteException;

    void addObserver(Observer observer) throws RemoteException;

    void removeObserver(Observer observer) throws RemoteException;
}
