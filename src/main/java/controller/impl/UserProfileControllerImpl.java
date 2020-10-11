package controller.impl;

import controller.UserProfileController;
import data.CurrentUser;
import exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.UpdateUserLoginRequest;
import request.UpdateUserPasswordRequest;
import response.UserResponse;
import runner.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileControllerImpl implements UserProfileController {
    @FXML
    Label loginLabel;

    @FXML
    Label emailLabel;

    @FXML
    Label dateLabel;

    @FXML
    PasswordField oldPasswordField;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField submitPasswordField;

    @FXML
    TextField loginField;

    @FXML
    Button submitPasswordButton;

    @FXML
    Button submitLoginPassword;

    @FXML
    Button changeEmailButton;

    @FXML
    Button deleteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentUserInfoToWindow();
        changeEmailButton.setOnAction(this::onChangeEmailClick);
        submitPasswordButton.setOnAction(this::changePassword);
        submitLoginPassword.setOnAction(this::changeLogin);
        deleteButton.setOnAction(this::delete);
    }

    public void setCurrentUserInfoToWindow() {
        String username = "Вы вошли под логином: " + CurrentUser.getUsername();
        loginLabel.setText(username);

        logger.info("Request was sent");
        ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().getUserInfo();
        logger.info("Answer received");

        if (answer.getStatusCode().is2xxSuccessful()) {
            String text = answer.getBody().getEmail();
            dateLabel.setText("Дата создания аккаунта: " + answer.getBody().getCreatedAt().toString());
            emailLabel.setText(text);
            logger.info("User's info got");
        } else {
            DialogProvider.showDialog("INFORMATION", "Email doesn't load", Alert.AlertType.INFORMATION , false);
        }
    }

    @Override
    @FXML
    public void changePassword(ActionEvent event){
        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest();

            if ((validatePasswordField())){
                if (validatePasswordEquals()) {
                    request.setOldPassword(oldPasswordField.getText());
                    request.setPassword(passwordField.getText());

                    logger.info("Request was sent");
                    ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().updateUserPassword(request);
                    logger.info("Answer received");

                    if (answer.getStatusCode().is2xxSuccessful()) {
                        oldPasswordField.clear();
                        passwordField.clear();
                        submitPasswordField.clear();
                        logger.info("User's password changed");
                        DialogProvider.showDialog("INFORMATION", "Password changed", Alert.AlertType.INFORMATION , false);
                    } else {
                        DialogProvider.showDialog("INFORMATION", "Password doesn't changed", Alert.AlertType.INFORMATION , false);
                    }
                } else {
                    logger.info("Fields aren't validate!");
                    DialogProvider.showDialog("WARNING", "Invalid password!", Alert.AlertType.WARNING, false);
                }
            } else {
                    logger.info("Fields aren't validate!");
                    DialogProvider.showDialog("WARNING", "Invalid password!", Alert.AlertType.WARNING , false);
                }
    }

    @Override
    @FXML
    public void changeLogin(ActionEvent event)  {
        UpdateUserLoginRequest request = new UpdateUserLoginRequest();
            if (validateLoginField()){
                request.setUsername(loginField.getText());

                logger.info("Request was sent");
                ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().updateUserLogin(request);
                logger.info("Answer received");
                if (answer.getStatusCode().is2xxSuccessful()) {
                    loginField.clear();
                    logger.info("User's login changed");

                    DialogProvider.showDialog("INFORMATION", "Login changed", Alert.AlertType.INFORMATION , false);

                    logger.info("Logout command");
                    CurrentUser.logOut();

                    Stage stageToClose = (Stage) deleteButton.getScene().getWindow();
                    stageToClose.close();

                    Main.StopThread();

                    Stage mainStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/logIn.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mainStage.setScene(new Scene(root, 620, 680));
                    mainStage.show();

                } else {
                    DialogProvider.showDialog("INFORMATION", "Login doesn't changed", Alert.AlertType.INFORMATION , false);
                }
            } else {
                logger.info("Fields aren't validate!");
                DialogProvider.showDialog("WARNING", "Invalid login!", Alert.AlertType.WARNING, false);
            }
    }

    @FXML
    private void onChangeEmailClick(ActionEvent event){
        Stage changeEmail = new Stage();
        Parent changeEmailSceneRoot = null;
        try {
            changeEmailSceneRoot = FXMLLoader.load(UserProfileControllerImpl.this.getClass().getResource("/changeEmail.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeEmail.setScene(new Scene(changeEmailSceneRoot, 620, 550));
        changeEmail.show();
    }

    @FXML
    public void delete(ActionEvent event){
        logger.info("Request was sent");
        ResponseEntity<String> answer = ServerConnectionProvider.getInstance().deleteAccount();
        logger.info("Answer received");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("User's account deleted");

            //AWAIT!!!!!!!!!!
            DialogProvider.showDialog("INFORMATION", "Account deleted", Alert.AlertType.INFORMATION , false);

            ((Stage)deleteButton.getScene().getWindow()).close();
            Stage.getWindows().forEach(win -> {((Stage)(win.getScene().getWindow())).close();});

            Stage applStage = new Stage();
            Parent applSceneRoot = null;
            try {
                applSceneRoot = FXMLLoader.load(UserProfileControllerImpl.this.getClass().getResource("/login.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            applStage.setScene(new Scene(applSceneRoot, 620, 680));
            applStage.show();
        } else {
            DialogProvider.showDialog("INFORMATION", "Account doesn't deleted", Alert.AlertType.INFORMATION , false);
        }
    }

    private boolean validateLoginField() {
        if (loginField.getText().length() < 4 && loginField.getText().equals(CurrentUser.getUsername())) {
            return false;
        } else
            return true;
    }

    private boolean validatePasswordEquals() {
        if (passwordField.getText().equals(oldPasswordField.getText())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePasswordField() {
        if (oldPasswordField.getText().length() < 4 && passwordField.getText().length() < 4) {
            return false;
        } else {
            return true;
        }
    }

}
