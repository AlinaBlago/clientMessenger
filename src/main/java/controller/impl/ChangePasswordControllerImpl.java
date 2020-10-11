package controller.impl;

import controller.ChangePasswordController;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.UserRequest;
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
        submitButton.setOnAction(this::changePassword);
    }

    @Override
    @FXML
    public void changePassword(ActionEvent event){
        if (loginField.getText().length() == 0){
            DialogProvider.showDialog("WARNING" , "Wrong Login");
        }

        UserRequest requestBody = new UserRequest(loginField.getText());
        ResponseEntity<ChangePasswordResponse> answer = ServerConnectionProvider.getInstance().getToken(requestBody);
        logger.info("Request was sent");

        CurrentUser.setUsername(answer.getBody().getUsername());
        CurrentUser.setChangePasswordToken(answer.getBody().getToken());
        System.out.println(CurrentUser.getUsername());

        if(answer.getStatusCode().is2xxSuccessful()){
        DialogProvider.showDialog("Successful" , "Token sent for your mail", Alert.AlertType.INFORMATION , false);

        openNewWindow();

        Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
        currentStageToClose.close();
        return;
    }else{
        DialogProvider.showDialog("ERROR" , "Something went wrong" , Alert.AlertType.ERROR , false);
    }
}

    public void openNewWindow(){
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
