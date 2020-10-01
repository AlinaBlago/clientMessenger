package controller.impl;

import controller.ChangePasswordController;
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
import request.SendChangePasswordTokenRequest;
import response.ChangePasswordResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordControllerImpl implements ChangePasswordController {

    @FXML
    TextField loginField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submitButton.setOnAction(this::onSubmitClick);
    }

    @FXML
    private void onSubmitClick(ActionEvent event){
        if (loginField.getText().length() == 0){
            DialogProvider.ShowDialog("WARNING" , "Wrong Login");
        }

        SendChangePasswordTokenRequest requestBody = new SendChangePasswordTokenRequest(loginField.getText());
        ResponseEntity<ChangePasswordResponse> answer = ServerConnectionProvider.getInstance().getToken(requestBody);
        logger.info("Request was sent");

        CurrentUser.setUsername(answer.getBody().getUsername());
        CurrentUser.setChangePasswordToken(answer.getBody().getToken());
        System.out.println(CurrentUser.getUsername());

        if(answer.getStatusCode().is2xxSuccessful()){
        DialogProvider.ShowDialog("Successful" , "Token sent for your mail", Alert.AlertType.INFORMATION);

        openNewWindow();

        Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
        currentStageToClose.close();
        return;
    }else{
        DialogProvider.ShowDialog("ERROR" , "Something went wrong" , Alert.AlertType.ERROR);
    }
}

    public void openNewWindow(){
        Stage changePassword = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/submitChangePassword.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changePassword.setScene(new Scene(root, 620, 680));
        changePassword.show();
    }
}
