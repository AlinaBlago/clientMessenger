package controller.impl;

import controller.LogUpController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        signUpButton.setOnAction(this::setSignUpClick);
    }

    @FXML
    public void setSignUpClick(ActionEvent event){
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

            logger.info("request sign up sending");
            StringBuffer url = new StringBuffer();
            url.append("http://localhost:8080/SignUp?name=");
            url.append(nameField.getText());
            url.append("&login=");
            url.append(loginField.getText());
            url.append("&password=");
            url.append(passwordField.getText());
            URL obj = new URL(url.toString());

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine = null;
            StringBuffer response = new StringBuffer();

            while (true) {
                if (!((inputLine = in.readLine()) != null)) break;
                response.append(inputLine);
            }

            in.close();


            logger.info("Request was sent");

//            Gson gson = new Gson();
//
//            AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);
//            if (response1.getResponseID() == 0) {
//                logger.info("Registration successful");
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Answer");
//                alert.setHeaderText("Results:");
//                alert.setContentText(response1.getResponseMessage());
//                alert.show();
//                Stage stage = (Stage) signUpButton.getScene().getWindow();
//                stage.close();
//                return;
//            }
//            if (response1.getResponseID() == 2) {
//                logger.warn("Registration failed");
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle(response1.getResponseMessage());
//                alert.setHeaderText("Results:");
//                alert.setContentText("Такой пользователь уже существует");
//                alert.show();
//                loginField.setText("");
//                nameField.setText("");
//                passwordField.setText("");
//                repeatPasswordField.setText("");
 //           }
        } catch (Exception e) {
            // TODO
        }
    }

}
