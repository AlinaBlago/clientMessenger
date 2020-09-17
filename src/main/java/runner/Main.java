package runner;

import controller.ApplicationController;
import data.CurrentUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Override
    public void start (Stage stage) throws Exception {
        logger.info("Application start");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logIn.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 620, 680));
        stage.show();
        logger.info("login.xml loaded");
        }

        //TODO
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        CurrentUser.ourThread.stop();
//        logger.info("Program finalized");
//    }

    public static void main(String[] args) {
        launch(args);
    }

}
