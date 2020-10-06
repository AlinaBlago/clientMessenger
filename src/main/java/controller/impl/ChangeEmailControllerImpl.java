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
import request.GetTokenForUpdateEmailRequest;

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
        submitButton.setOnAction(this::onSubmitClick);
    }

    @FXML
    private void onSubmitClick(ActionEvent event){
        if (emailField.getText().length() == 0){
            DialogProvider.ShowDialog("WARNING" , "Wrong Login");
        }

        GetTokenForUpdateEmailRequest requestBody = new GetTokenForUpdateEmailRequest(emailField.getText());
        ResponseEntity<String> answer = ServerConnectionProvider.getInstance().getTokenForChangingEmail(requestBody);
        logger.info("Request was sent");

        CurrentUser.setChangeEmailToken(answer.getBody());

        if(answer.getStatusCode().is2xxSuccessful()){
            CurrentUser.setEmail(emailField.getText());
            DialogProvider.ShowDialog("Successful" , "Token sent for your mail", Alert.AlertType.INFORMATION);

            openNewWindow();

            Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
            currentStageToClose.close();
            return;
        }else{
            DialogProvider.ShowDialog("ERROR" , "Something went wrong" , Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openNewWindow(){
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
