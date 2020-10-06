package controller.impl;

import controller.SubmitChangeEmail;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.ChangeEmailRequest;
import request.ChangePasswordRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class SubmitChangeEmailImpl implements SubmitChangeEmail {
    @FXML
    TextField tokenField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submitButton.setOnAction(this::onSubmitClick);
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        ChangeEmailRequest requestBody = new ChangeEmailRequest(tokenField.getText(), CurrentUser.getEmail());
        ResponseEntity<String> answer = ServerConnectionProvider.getInstance().changeEmail(requestBody);
        logger.info("Request was sent");

        if (answer.getStatusCode().is2xxSuccessful()) {
            DialogProvider.ShowDialog("Successful", "Email changed", Alert.AlertType.INFORMATION);

            Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
            currentStageToClose.close();
            return;
        } else {
            DialogProvider.ShowDialog("ERROR", "Something went wrong", Alert.AlertType.ERROR);
        }
    }


}
