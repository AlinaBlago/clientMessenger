package controller.impl;

import controller.LogUpController;
import data.ServerArgument;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.RequestType;
import providers.ServerConnectionProvider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
                if (nameField.getText().length() < 1)
                    throw new Exception("Please enter name more than 1 symbol!");
                if (loginField.getText().length() < 2)
                    throw new Exception("Please enter login more than 2 symbols!");
                if (passwordField.getText().length() < 4)
                    throw new Exception("Password cannot be less than 4 symbols!");
                if (!passwordField.getText().equals(repeatPasswordField.getText()))
                    throw new Exception("Passwords are not equal!");
            } catch (Exception e) {
                logger.warn(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Wrong data");
                alert.setContentText(e.getMessage());
                alert.show();
                return;
            }

            logger.info("Request sign up sending");

            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login" , loginField.getText()));
            argumentsList.add(new ServerArgument("password" , passwordField.getText()));

            ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("login", argumentsList, RequestType.GET);

            logger.info("Request was sent");

            if (answer.getBody() == 0) {
                logger.info("Registration successful");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Answer");
                alert.setHeaderText("Results:");
                alert.setContentText(String.valueOf(answer.getStatusCode()));
                alert.show();
                Stage stage = (Stage) signUpButton.getScene().getWindow();
                stage.close();
                return;
            }
            if (answer.getBody() == 2) {
                logger.warn("Registration failed");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(String.valueOf(answer.getStatusCode()));
                alert.setHeaderText("Results:");
                alert.setContentText("Такой пользователь уже существует");
                alert.show();
                loginField.setText("");
                nameField.setText("");
                passwordField.setText("");
                repeatPasswordField.setText("");
            }
        } catch (Exception e) {
            // TODO
        }
    }

}
