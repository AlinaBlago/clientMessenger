package controller.impl;

import controller.LogInController;
import data.CurrentUser;
import data.ServerArgument;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import providers.DialogProvider;
import providers.RequestType;
import providers.ServerConnectionProvider;
import request.LoginRequest;
import request.SignupRequest;
import response.LoginResponse;
import response.SignupResponse;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.*;

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
        loginButton.setOnAction(this::onLoginClick);
        signUpButton.setOnAction(this::onSignUpClick);
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        try {
            if(loginField.getText().length() == 0 || passwordField.getText().length() == 0){
                return;
            }

//            List<ServerArgument> argumentsList = new ArrayList<>();
//            argumentsList.add(new ServerArgument("login" , loginField.getText()));
//            argumentsList.add(new ServerArgument("password" , passwordField.getText()));
            LoginRequest requestBody = new LoginRequest(loginField.getText() , passwordField.getText());
            ResponseEntity<LoginResponse> answer = ServerConnectionProvider.getInstance().loginRequest(requestBody);

            if(answer.getStatusCode().is2xxSuccessful() && answer.getBody().isStatus()){
                DialogProvider.ShowDialog("SUCCESSFUL" , "User logged in");
                logger.info("User logged in");

                //Открываем главное окно
                Stage applStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/application.fxml"));
                Parent root = loader.load();
                applStage.setScene(new Scene(root, 620, 680));
                applStage.show();

                //Закрываем текущее окно
                Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
                currentStageToClose.close();
                return;
            }else{
                DialogProvider.ShowDialog("ERROR" , "Wrong login or password" , Alert.AlertType.ERROR);
            }

//            ResponseEntity answer = ServerConnectionProvider.getInstance().loginRequest("application", argumentsList, RequestType.POST);

            User user = new User(loginField.getText(), passwordField.getText());
            CurrentUser.init(user);

//            if (Objects.equals(answer.getStatusCode(), HttpStatus.FOUND)) {
//                logger.info("User is logged in");
//
//                //Открываем главное окно
//                Stage applStage = new Stage();
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(getClass().getResource("/application.fxml"));
//                Parent root = loader.load();
//                applStage.setScene(new Scene(root, 620, 680));
//                applStage.show();
//
//                //Закрываем текущее окно
//                Stage currentStageToClose = (Stage) signUpButton.getScene().getWindow();
//                currentStageToClose.close();
//                return;
//            }
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setContentText("Wrong login or password");
//            alert.show();

        } catch (Exception e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onSignUpClick(ActionEvent event){
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
