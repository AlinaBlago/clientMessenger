package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface LogUpController extends Initializable {

    @FXML
    TextField nameField = new TextField();

    @FXML
    TextField loginField = new TextField();

    @FXML
    Button signUpButton = new Button();

    @FXML
    PasswordField passwordField = new PasswordField();

    @FXML
    PasswordField repeatPasswordField = new PasswordField();

    Logger logger = LoggerFactory.getLogger(LogInController.class);

    void initialize();
}
