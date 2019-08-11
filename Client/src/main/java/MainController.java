import domain.*;
import enums.UserType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import services.Observer;
import services.ServerInterface;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Collection;

public class MainController extends UnicastRemoteObject implements Observer {

    private Stage primaryStage;
    private ServerInterface server;
    private User user;
    private ParkingLot selectedParkingLot;

    @FXML
    private Button adminButton;
    @FXML
    private RadioButton radioButtonDay;
    @FXML
    private RadioButton radioButtonWeek;
    @FXML
    private RadioButton radioButtonMonth;
    @FXML
    private TabPane tabPaneView;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private ImageView imageViewLogo;

    public MainController() throws RemoteException {
    }

    public void init(ServerInterface server, Stage primaryStage, User user) throws RemoteException {
        this.primaryStage = primaryStage;
        this.server = server;
        this.user = user;
        if (!user.getUserType().equals(UserType.Admin)) {
            adminButton.setVisible(false);
            adminButton.setDisable(true);
        }
        radioButtonDay.fire();
        initDatePickers();
        initTabPaneView();

        File file = new File(MainController.class.getClassLoader().getResource("logo.png").getFile());
        Image image = new Image(file.toURI().toString());
        imageViewLogo.setImage(image);
    }

    // TODO: 8/11/2019 refactor
    private void initTabPaneView() throws RemoteException {
        Collection<Site> sites = server.getSites();

        tabPaneView.getTabs().clear();
        for (Site site : sites) {
            Tab tab = new Tab();
            tab.setText(site.getName());

            GridPane parkingLotsPane = new GridPane();

            Collection<Reservation> reservations = server.getReservationsForDate(datePickerStart.getValue());

            for (ParkingLotPositioned parkingLot : site.getParkingLots()) {
                Reservation reservation = reservations.stream()
                        .filter(x -> x.getParkingLot()
                                .equals(parkingLot.getParkingLot()))
                        .findFirst()
                        .orElse(null);
                parkingLotsPane.add(createParkingLotComponent(reservation, parkingLot.getParkingLot()), parkingLot.getxPosition(), parkingLot.getyPosition());
            }

            parkingLotsPane.setHgap(5);
            parkingLotsPane.setVgap(5);
            tab.setContent(parkingLotsPane);
            tabPaneView.getTabs().add(tab);
        }
    }

    // TODO: 8/11/2019 refactor
    private Pane createParkingLotComponent(Reservation reservation, ParkingLot parkingLot) {
        Pane pane = new Pane();
        pane.setPrefHeight(80);
        pane.setPrefWidth(80);
        Label labelParkingName = new Label();
        labelParkingName.setText(parkingLot.getName());
        labelParkingName.setTextFill(Paint.valueOf("#ffffff"));
        pane.getChildren().add(labelParkingName);
        if (reservation != null) {
            Label label = new Label();
            label.setText(reservation.getUser().getUserName());
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(14));
            label.setTextFill(Paint.valueOf("#ffffff"));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefHeight(80);
            label.setPrefWidth(80);
            pane.getChildren().add(label);
            pane.setStyle("-fx-background-color: #8B0000");
        } else {
            pane.setStyle("-fx-background-color: #006400");
        }
        boolean available = true;
        if (reservation != null) {
            available = false;
        }
        pane.getProperties().put("parkingLot", parkingLot);
        pane.getProperties().put("available", available);
        pane.setOnMouseClicked(e -> {
            pane.setStyle("-fx-background-color: #786349");
            ((GridPane) pane.getParent()).getChildren().forEach(x -> {
                if (!x.equals(pane)) {
                    if (x.getProperties().get("available") == Boolean.valueOf(true)) {
                        x.setStyle("-fx-background-color: #006400");
                    } else {
                        x.setStyle("-fx-background-color: #8B0000");
                    }
                }
            });
            selectedParkingLot = (ParkingLot) pane.getProperties().get("parkingLot");
        });

        return pane;
    }

    private void initDatePickers() {
        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.now());
        datePickerStart.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.isAfter(datePickerEnd.getValue())) {
                datePickerEnd.setValue(newValue);
            }
            try {
                initTabPaneView();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        datePickerEnd.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.isBefore(datePickerStart.getValue())) {
                datePickerStart.setValue(newValue);
            }
        });
    }

    @FXML
    private void logoutHandler() throws RemoteException {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartClient.class.getResource("loginView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        LoginController ctrl = loader.getController();
        stage.setScene(new Scene(root));
        ctrl.init(server, stage);

        stage.show();
        primaryStage.hide();
        server.logout(this);

    }

    private int getStep() {
        int step = 1;
        if (radioButtonWeek.isSelected()) {
            step = 7;
        } else if (radioButtonMonth.isSelected()) {
            step = 30;
        }
        return step;
    }

    @FXML
    private void reserveHandler() throws RemoteException {

        if (selectedParkingLot != null) {
            server.makeReservations(user, selectedParkingLot, datePickerStart.getValue(), datePickerEnd.getValue(), getStep());
        }
        initTabPaneView();
    }

    @FXML
    private void releaseHandler() throws RemoteException {
        if (selectedParkingLot != null) {
            server.removeReservationForDate(user, selectedParkingLot, datePickerStart.getValue(), datePickerEnd.getValue(), getStep());
        }
        initTabPaneView();
    }

    @FXML
    private void adminHandler() throws RemoteException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartClient.class.getResource("adminView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-2);
        }
        AdminController ctrl = loader.getController();
        stage.setScene(new Scene(root));
        ctrl.init(server);
        stage.show();
    }

    @Override
    public void update() throws RemoteException {
        Platform.runLater(() -> {
            try {
                initTabPaneView();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void logout() throws RemoteException {
        logoutHandler();
    }
}
