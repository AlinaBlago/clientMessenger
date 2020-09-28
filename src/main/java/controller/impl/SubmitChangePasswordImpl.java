package controller.impl;

import controller.SubmitChangePassword;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SubmitChangePasswordImpl implements SubmitChangePassword {
    @FXML
    TextField loginField;

    @FXML
    Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
