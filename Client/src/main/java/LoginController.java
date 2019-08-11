import domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class LoginController {

    private Stage primaryStage;
    private ServerInterface server;

    @FXML
    private TextField textFieldUserName;
    @FXML
    private PasswordField passwordFieldPassword;

    public void init(ServerInterface server, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.server = server;

        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    // TODO: 8/11/2019 refactor
    @FXML
    private void loginHandler() throws RemoteException {

        User user;
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartClient.class.getResource("mainView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        MainController ctrl = loader.getController();

        stage.setOnCloseRequest(event -> {
            try {
                ctrl.logout();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });

        try {
            user = server.login(textFieldUserName.getText(), passwordFieldPassword.getText(), ctrl);
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        if (user != null) {

            stage.setScene(new Scene(root));
            stage.show();
            ctrl.init(server, stage, user);

            primaryStage.hide();
        }

    }

}
