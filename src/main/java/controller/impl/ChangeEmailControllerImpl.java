package controller.impl;

import controller.ChangeEmailController;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import response.UserResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangeEmailControllerImpl implements ChangeEmailController {
    @FXML
    TextField emailField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submitButton.setOnAction(this::onEmailClick);
    }

    @FXML
    private void onEmailClick(ActionEvent event){
        Stage emailStage = new Stage();
        Parent changeEmailSceneRoot = null;
        try {
            changeEmailSceneRoot = FXMLLoader.load(ChangeEmailControllerImpl.this.getClass().getResource("/submitChangeEmail.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        emailStage.setScene(new Scene(changeEmailSceneRoot, 620, 550));
        emailStage.show();
    }

}
