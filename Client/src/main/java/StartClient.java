import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ServerInterface;

public class StartClient extends Application {

    public void start(Stage primaryStage) throws Exception {

        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:client.xml");
        ServerInterface server = (ServerInterface) factory.getBean("server");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartClient.class.getResource("loginView.fxml"));
        Parent root = loader.load();
        LoginController ctrl = loader.getController();
        primaryStage.setScene(new Scene(root));
        ctrl.init(server, primaryStage);

        primaryStage.show();

    }
}
