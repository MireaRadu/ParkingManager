import domain.ParkingLot;
import domain.ParkingLotPositioned;
import domain.Site;
import domain.User;
import enums.UserType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import services.Observer;
import services.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

public class AdminController extends UnicastRemoteObject implements Observer {

    @FXML
    private TextField textFieldUserName;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldSiteName;
    @FXML
    private TextField textFieldParkingLotName;
    @FXML
    private Spinner<Integer> spinnerXPos;
    @FXML
    private Spinner<Integer> spinnerYPos;
    @FXML
    private ComboBox<Site> comboBoxSite;
    @FXML
    private ComboBox<Site> comboBoxRemoveSite;
    @FXML
    private ComboBox<Site> comboBoxRemoveParkingLotSite;
    @FXML
    private ComboBox<ParkingLotPositioned> comboBoxRemoveParkingLot;
    @FXML
    private ComboBox<User> comboBoxRemoveUser;
    @FXML
    private ComboBox<UserType> comboBoxUserType;

    private ServerInterface server;


    public AdminController() throws RemoteException {
    }

    public void init(ServerInterface server) throws RemoteException {
        this.server = server;
        server.addObserver(this);
        initComponents();
    }

    private void initComponents() throws RemoteException {
        initComboBoxes();
        initSpinners();
    }

    private void initComboBoxes() throws RemoteException {
        ObservableList<User> userList = FXCollections.observableList((List<User>) server.getUsers());
        comboBoxRemoveUser.setItems(userList);
        comboBoxRemoveUser.getSelectionModel().select(0);
        comboBoxUserType.setItems(FXCollections.observableList(Arrays.asList(UserType.values())));
        comboBoxUserType.getSelectionModel().select(0);
        ObservableList<Site> siteList = FXCollections.observableList((List<Site>) server.getSites());
        comboBoxRemoveSite.setItems(siteList);
        comboBoxRemoveSite.getSelectionModel().select(0);
        comboBoxRemoveParkingLotSite.setItems(siteList);
        comboBoxRemoveParkingLotSite.getSelectionModel().select(0);
        initParkingLotComboBox();
        // FIXME: 8/11/2019 update combo parking for combo site
        comboBoxRemoveParkingLotSite.getSelectionModel()
                .selectedItemProperty()
                .addListener((options, oldValue, newValue) -> {
                }/*initParkingLotComboBox()*/);
        comboBoxSite.setItems(siteList);
        comboBoxSite.getSelectionModel().select(0);

    }

    private void initParkingLotComboBox() {
        comboBoxRemoveParkingLot.setItems(FXCollections
                .observableList((List<ParkingLotPositioned>) comboBoxRemoveParkingLotSite
                        .getSelectionModel()
                        .getSelectedItem()
                        .getParkingLots()));
        comboBoxRemoveParkingLot.getSelectionModel().select(0);
    }

    private void initSpinners() {
        spinnerXPos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        spinnerYPos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        spinnerXPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinnerXPos.increment(0);
            }
        });
        spinnerYPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinnerYPos.increment(0);
            }
        });
    }

    @FXML
    private void addUserHandler() throws RemoteException {
        String username = textFieldUserName.getText();
        String password = textFieldPassword.getText();
        if (!username.isEmpty() && !password.isEmpty()) {
            User user = new User(username, password, comboBoxUserType.getValue());
            server.addUser(user);
        }
    }

    @FXML
    private void addSiteHandler() throws RemoteException {
        String name = textFieldSiteName.getText();
        if (!name.isEmpty()) {
            Site site = new Site(name);
            server.addSite(site);
        }
    }

    @FXML
    private void addParkingLotHandler() throws RemoteException {
        String name = textFieldParkingLotName.getText();
        int xPos = spinnerXPos.getValue();
        int yPos = spinnerYPos.getValue();
        Site site = comboBoxSite.getValue();

        if (!name.isEmpty() && xPos > 0 && yPos > 0 && site != null) {
            ParkingLot parkingLot = new ParkingLot(name);
            server.addParkingLotToSite(site, new ParkingLotPositioned(parkingLot, xPos, yPos));
        }
    }

    @FXML
    private void removeUserHandler() throws RemoteException {
        User user = comboBoxRemoveUser.getValue();
        if (user != null) {
            server.removeUser(user);
        }
    }

    @FXML
    private void removeSiteHandler() throws RemoteException {
        Site site = comboBoxRemoveSite.getValue();
        if (site != null) {
            server.removeSite(site);
        }
    }

    @FXML
    private void removeParkingLotHandler() throws RemoteException {
        Site site = comboBoxRemoveParkingLotSite.getValue();
        ParkingLotPositioned parkingLot = comboBoxRemoveParkingLot.getValue();
        if (site != null && parkingLot != null) {
            server.removeParkingLotFromSite(site, parkingLot);
        }
    }


    @Override
    public void update() {
        Platform.runLater(() -> {
            try {
                AdminController.this.initComponents();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }


}
