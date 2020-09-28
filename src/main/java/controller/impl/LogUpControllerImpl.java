package controller.impl;

import controller.LogUpController;
import data.ServerArgument;
import exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.RequestType;
import providers.ServerConnectionProvider;
import request.SignupRequest;
import response.SignupResponse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LogUpControllerImpl implements LogUpController {

    @FXML
    TextField nameField;

    @FXML
    TextField loginField;

    @FXML
    Button signUpButton;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField repeatPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setOnAction(this::onSignUpClick);
    }

    @Override
    @FXML
    public void onSignUpClick(ActionEvent event){
        try {
            try {
                validateSignUpFields();
            } catch (ValidationException e) {
                logger.warn(e.getMessage());
                DialogProvider.ShowDialog(e.GetPropertyName() , e.GetPropertyError());
                return;
            }
            logger.info("Request sign up sending");
            SignupRequest requestBody = new SignupRequest(nameField.getText() , loginField.getText() , passwordField.getText());
            ResponseEntity<SignupResponse> answer = ServerConnectionProvider.getInstance().signUpRequest(requestBody);
            logger.info("Request was sent");

            if(answer.getStatusCode().is2xxSuccessful() && answer.getBody().isStatus()){
                DialogProvider.ShowDialog("SUCCESSFUL" , "New user created");
            }else{
                DialogProvider.ShowDialog("ERROR" , "Something went wrong" , Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private void validateSignUpFields() throws ValidationException {
        if (nameField.getText().length() < 1)
            throw new ValidationException("" , "Name" , "enter name more than 1 symbol!");
        if (loginField.getText().length() < 2)
            throw new ValidationException("" , "login" , "enter name more than 2 symbol!");
        if (passwordField.getText().length() < 4)
            throw new ValidationException("" , "Password" , "cannot be less than 4 symbols!");
        if (!passwordField.getText().equals(repeatPasswordField.getText()))
            throw new ValidationException("" , "Password" , "password must be equal!");
    }

}
