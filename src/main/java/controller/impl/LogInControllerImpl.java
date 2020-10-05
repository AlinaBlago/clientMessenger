package controller.impl;

import controller.LogInController;
import data.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.LoginRequest;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInControllerImpl implements LogInController {
    @FXML
    Button loginButton;

    @FXML
    TextField loginField;

    @FXML
    Button signUpButton;

    @FXML
    PasswordField passwordField;

    @FXML
    Button changePasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::onLoginClick);
        signUpButton.setOnAction(this::onSignUpClick);
        changePasswordButton.setOnAction(this::onChangePasswordClick);
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        try {
            if(loginField.getText().length() > 0 || passwordField.getText().length() > 0) {

                LoginRequest requestBody = new LoginRequest(loginField.getText(), passwordField.getText());

                logger.info("Request was sent");
                ResponseEntity<String> answer = ServerConnectionProvider.getInstance().loginRequest(requestBody);
                logger.info("Answer received");

                if (answer.getStatusCode().is2xxSuccessful()) {
                    CurrentUser.setUsername(loginField.getText());

                    logger.info("User is logged in");

                    CurrentUser.setAuthToken(answer.getHeaders().get("Authorization").get(0));
                    //RETURN LOGIN ON SERVER WHEN LOGIN
                    Stage applStage = new Stage();
                    Parent applSceneRoot = null;
                    try {
                        applSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/application.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    applStage.setScene(new Scene(applSceneRoot, 620, 680));
                    applStage.show();

                    ((Stage) loginButton.getScene().getWindow()).close();
                   // NavigationProvider.NavigateToMainForm((Stage)signUpButton.getScene().getWindow());
                }else{
                    DialogProvider.ShowDialog("ERROR", "Wrong login or password", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onSignUpClick(ActionEvent event){
        Stage signUp = new Stage();
        Parent signUpSceneRoot = null;
        try {
            signUpSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/logUp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUp.setScene(new Scene(signUpSceneRoot, 620, 680));
        signUp.show();
    }

    @FXML
    private void onChangePasswordClick(ActionEvent event){

        Stage changePassword = new Stage();
        Parent changePasswordSceneRoot = null;
        try {
            changePasswordSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/changePassword.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        changePassword.setScene(new Scene(changePasswordSceneRoot, 620, 570));
        changePassword.show();
    }
}
