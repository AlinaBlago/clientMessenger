package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ChangePasswordController extends Initializable {
    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    Stage changePassword = new Stage();
    FXMLLoader loader = new FXMLLoader();

    void changePassword(ActionEvent event);
}
