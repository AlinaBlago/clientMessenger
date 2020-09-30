package controller.impl;

import controller.SubmitChangePassword;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.ChangePasswordRequest;
import request.SendChangePasswordTokenRequest;
import response.ChangePasswordResponse;

import java.net.URL;
import java.util.ResourceBundle;

public class SubmitChangePasswordImpl implements SubmitChangePassword {
    @FXML
    TextField codeField;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField submitPasswordField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submitButton.setOnAction(this::onSubmitClick);
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        System.out.println(CurrentUser.getUsername());
        ChangePasswordRequest requestBody = new ChangePasswordRequest(codeField.getText(), passwordField.getText(), CurrentUser.getUsername());
        ResponseEntity<String> answer = ServerConnectionProvider.getInstance().changePassword(requestBody);
        logger.info("Request was sent");
        System.out.println( codeField.getText());
        System.out.println(passwordField.getText());

        if (answer.getStatusCode().is2xxSuccessful()) {
            DialogProvider.ShowDialog("Successful", "Password changed", Alert.AlertType.INFORMATION);

            Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
            currentStageToClose.close();
            return;
        } else {
            DialogProvider.ShowDialog("ERROR", "Something went wrong", Alert.AlertType.ERROR);
        }
    }
}
