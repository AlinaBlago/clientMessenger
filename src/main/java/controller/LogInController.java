package controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface LogInController extends Initializable {

    @FXML
    TextField loginField = new TextField();

    @FXML
    Button loginButton = new Button();

    @FXML
    Button signUpButton = new Button();

    @FXML
    PasswordField passwordField = new PasswordField();

    Logger logger = LoggerFactory.getLogger(LogInController.class);

    @FXML
    void initialize() throws IOException;

}
