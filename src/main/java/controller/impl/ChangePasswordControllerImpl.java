package controller.impl;

import controller.ChangePasswordController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        submitButton.setOnAction(this::onSubmitClick);
    }

    @FXML
    private void onSubmitClick(ActionEvent event){
        if (loginField.getText().equals(null)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Wrong login");
            alert.show();
        }

        Stage changePassword = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/submitChangePassword.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changePassword.setScene(new Scene(root, 620, 680));
        changePassword.show();

        Stage currentStageToClose = (Stage) submitButton.getScene().getWindow();
        currentStageToClose.close();
        return;
    }
}
