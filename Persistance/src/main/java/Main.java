import domain.User;
import enums.UserType;
import repository.RepositoryReservation;
import repository.RepositorySite;
import repository.RepositoryUser;

public class Main {
    public static void main(String[] args) {
        RepositoryUser repositoryUser = new RepositoryUser();
        RepositorySite repositorySite = new RepositorySite();
        RepositoryReservation repositoryReserv = new RepositoryReservation();
        /*Collection<Site> sites = repositorySite.getSites();
        repositorySite.addParkingLotToSite((Site)sites.toArray()[0], new ParkingLotPositioned(new ParkingLot("1a"), 1,1));
        repositorySite.addParkingLotToSite((Site)sites.toArray()[0], new ParkingLotPositioned(new ParkingLot("2a"), 2,1));
        repositorySite.addParkingLotToSite((Site)sites.toArray()[0], new ParkingLotPositioned(new ParkingLot("1b"), 2,2));*/
        repositoryUser.addUser(new User("c", "c", UserType.Admin));
    }
}
