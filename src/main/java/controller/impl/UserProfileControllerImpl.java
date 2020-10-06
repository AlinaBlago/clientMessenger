package controller.impl;

import controller.UserProfileController;
import data.CurrentUser;
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
import request.UpdateUserRequest;
import response.UserResponse;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentUserInfoToWindow();
        changeEmailButton.setOnAction(this::onChangeEmailClick);
        submitPasswordButton.setOnAction(this::onChangePasswordClick);
        submitLoginPassword.setOnAction(this::onChangeLoginClick);
    }

    public void setCurrentUserInfoToWindow() {
        String username = "Вы вошли под логином: " + CurrentUser.getUsername();
        loginLabel.setText(username);

        logger.info("Request was sent");
        ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().getUserInfo();
        logger.info("Answer received");

        if (answer.getStatusCode().is2xxSuccessful()) {
            String text = answer.getBody().getEmail();
            dateLabel.setText("Дата создания аккаунта: " + answer.getBody().getCreatedAt());
            emailLabel.setText(text);
            logger.info("User's info got");
        } else {
            DialogProvider.ShowDialog("INFORMATION", "Email doesn't load", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void onChangePasswordClick(ActionEvent event){
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPassword(passwordField.getText());
        request.setUsername(null);

        logger.info("Request was sent");
        ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().updateUser(request);
        logger.info("Answer received");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("User's password changed");
            DialogProvider.ShowDialog("INFORMATION", "Password changed", Alert.AlertType.INFORMATION);
        } else {
            DialogProvider.ShowDialog("INFORMATION", "Password doesn't changed", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void onChangeLoginClick(ActionEvent event){
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPassword(null);
        request.setUsername(loginField.getText());

        logger.info("Request was sent");
        ResponseEntity<UserResponse> answer = ServerConnectionProvider.getInstance().updateUser(request);
        logger.info("Answer received");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("User's login changed");
            DialogProvider.ShowDialog("INFORMATION", "Login changed", Alert.AlertType.INFORMATION);
        } else {
            DialogProvider.ShowDialog("INFORMATION", "Login doesn't changed", Alert.AlertType.INFORMATION);
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


}
