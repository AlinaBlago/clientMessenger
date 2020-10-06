package controller.impl;

import controller.SubmitChangeEmail;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SubmitChangeEmailImpl implements SubmitChangeEmail {
    @FXML
    TextField tokenField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
