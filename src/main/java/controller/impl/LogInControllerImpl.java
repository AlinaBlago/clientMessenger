package controller.impl;

import com.google.gson.Gson;
import controller.LogInController;
import data.CurrentUser;
import data.ServerArgument;
import data.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import providers.RequestType;
import providers.ServerConnectionProvider;
import serverResponse.AuthorizationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LogInControllerImpl implements LogInController {
    @FXML
    Button loginButton;

    @FXML
    TextField loginField;

    @FXML
    Button signUpButton;

    @FXML
    PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(this::OnLoginClick);
        signUpButton.setOnAction(this::OnSignUpClick);
    }


    @FXML
    private void OnLoginClick(ActionEvent event)
    {
        try {
            if(loginField.getText().length() == 0 || passwordField.getText().length() == 0){
                return;
            }

            List<ServerArgument> argumentsList = new ArrayList<>();
            argumentsList.add(new ServerArgument("login" , loginField.getText()));
            argumentsList.add(new ServerArgument("password" , passwordField.getText()));

            ResponseEntity answer = ServerConnectionProvider.sendRequest("Login" , argumentsList , RequestType.GET);

            Gson gson = new Gson();
            User user = new User("" , loginField.getText() , "");
            AuthorizationResponse response1 = gson.fromJson(answer.toString(), AuthorizationResponse.class);
            CurrentUser.init(user , response1.getResponseMessage());

            if(response1.getResponseID() == 0) {
                logger.info("User logined");

                //Открываем главное окно
                Stage applStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/appl.fxml"));
                Parent root = loader.load();
                applStage.setScene(new Scene(root, 620, 680));
                applStage.show();

                //Закрываем текущее окно
                Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
                currentStageToClose.close();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Wrong login or password");
                alert.show();
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }


    @FXML
    private void OnSignUpClick(ActionEvent event){
        Stage signUp = new Stage();
        Parent signUpSceneRoot = null;
        try {
            signUpSceneRoot = FXMLLoader.load(LogInControllerImpl.this.getClass().getResource("/logUp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUp.setScene(new Scene(signUpSceneRoot, 620, 680));
        signUp.show();
    }
}
