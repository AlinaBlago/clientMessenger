package controller.impl;

import com.google.gson.Gson;
import controller.LogUpController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogUpControllerImpl implements LogUpController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initialize() {
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    if (nameField.getText().length() < 1) throw new Exception("Please enter name more than 1 symbol!");
                    if (loginField.getText().length() < 2) throw new Exception("Please enter login more than 2 symbols!");
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
                URL obj = null;
                try {
                    obj = new URL(url.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) obj.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String inputLine = null;
                StringBuffer response = new StringBuffer();

                while (true) {
                    try {
                        if (!((inputLine = in.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.append(inputLine);
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logger.info("request was sent");

                Gson gson = new Gson();

//                AuthorizationResponse response1 = gson.fromJson(response.toString() , AuthorizationResponse.class);
//                if(response1.getResponseID() == 0){
//                    logger.info("registration successful");
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Answer");
//                    alert.setHeaderText("Results:");
//                    alert.setContentText(response1.getResponseMessage());
//                    alert.show();
//                    Stage stage = (Stage) signUpButton.getScene().getWindow();
//                    stage.close();
//                    return;
//                }
//                if(response1.getResponseID() == 2){
//                    logger.warn("registration failed");
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle(response1.getResponseMessage());
//                    alert.setHeaderText("Results:");
//                    alert.setContentText("Такой пользователь уже существует");
//                    alert.show();
//                    loginField.setText("");
//                    nameField.setText("");
//                    passwordField.setText("");
//                    repeatPasswordField.setText("");
//                }
            }
        });
    }
}
