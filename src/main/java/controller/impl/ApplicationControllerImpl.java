package controller.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

public class ApplicationControllerImpl implements ApplicationController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logOut();
            }
        });

        findUserButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                findUser();
            }
        });

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                send();
            }
        });

        usersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                usersListViewChanged(newValue);
            }
        });

        bindThreadCheckNewMessages();

        try {
            loadUserChats();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        setCurrentUserNameToWindow();
    }

    @Override
    public void usersListViewChanged(String newValue) {
        try {
            UpdateChatForUser(newValue);
        } catch (IOException e) {
          //  TODO
            e.printStackTrace();
        }
    }

    @Override
    public void send() {
        if (usersListView.getSelectionModel().isEmpty() == true){
            logger.info("Send function call: user is empty");
            return;
        }
        else{
            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
            if(sendMessageField.getText().length() > 0){
                boolean isExistsOnlyOfSpace = true;

                for(Character symbol : sendMessageField.getText().toCharArray()){
                    if(!symbol.equals(' ')){
                        isExistsOnlyOfSpace = false;
                        break;
                    }
                }

                if(isExistsOnlyOfSpace){
                    logger.info("Entered message text consist only of space");
                    return;
                } else {
                    try {
                        logger.info("Staring send 'sendMessage' to server");
                        StringBuffer url = new StringBuffer();
                        url.append("http://localhost:8080/sendMessage?senderLogin=");
                        url.append(CurrentUserInfo.getCurrentUser().getLogin());
                        url.append("&senderKey=");
                        url.append(CurrentUserInfo.getCurrentKey());
                        url.append("&receiverLogin=");
                        url.append(users_listview.getSelectionModel().getSelectedItem());
                        url.append("&message=");
                        String mesg = sendMessageField.getText().replaceAll(" " , "%20");
                        url.append(mesg);

                        URL obj = new URL(url.toString());
                        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                        connection.setRequestMethod("GET");

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        logger.info("Request 'sendMessage' sent" );

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String dateFormatted = formatter.format(System.currentTimeMillis());
                        chatListView.getItems().add(dateFormatted + " " + CurrentUserInfo.getCurrentUser().getLogin() + " : " + sendMessageField.getText());

                        int index = chatListView.getItems().size() - 1;
                        chatListView.scrollTo(index);

                        sendMessageField.setText("");

                    } catch (Exception e){
                        logger.warn(e.getMessage());
                        System.out.println(e.getMessage());
                    }

                }

            }else {
                return;
            }
            logger.info("Entered message text less than 0 symbols");
        }
    }

    @Override
    public void findUser() {
        if (findUserLogin.getText().length() == 0){
            return;
        }

        try {
            logger.info("Start send 'findUser' to server");
            StringBuffer url = new StringBuffer();
            url.append("http://localhost:8080/isUserExists?senderLogin=");
            url.append(CurrentUserInfo.getCurrentUser().getLogin());
            url.append("&senderKey=");
            url.append(CurrentUserInfo.getCurrentKey());
            url.append("&findUserLogin=");
            url.append(findUserLogin.getText());

            URL obj = new URL(url.toString());
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            logger.info("request sended");
            Gson gson = new Gson();

            AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);

            if(response1.getResponseID() == 0){
                logger.info("Response 0 from server");
                usersListView.getItems().add(findUserLogin.getText());
                usersListView.refresh();
            }else {
                logger.warn("Response not 0 from server : " + response1.getResponseMessage());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("User not found");
                alert.show();
            }
        } catch (Exception e){
            logger.warn(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCurrentUserNameToWindow() {
        String text = "Вы вошли под логином: " + CurrentUserInfo.getCurrentUser().getLogin();
        currentUserNameLabel.setText(text);
    }

    @Override
    public void bindThreadCheckNewMessages() {
        Task task = new Task() {
            @Override
            protected Void call() throws IOException {
                do {
                    StringBuffer url = new StringBuffer();
                    url.append("http://localhost:8080/haveNewMessages?senderLogin=");
                    url.append(CurrentUserInfo.getCurrentUser().getLogin());
                    url.append("&senderKey=");
                    url.append(CurrentUserInfo.getCurrentKey());

                    URL obj = new URL(url.toString());
                    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Gson gson = new Gson();

                    AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);

                    if (response1.getResponseID() == 0) {
                        Type listType = new TypeToken<Set<String>>() {
                        }.getType();
                        Set<String> users = gson.fromJson(response1.getResponseMessage(), listType);
                    }

                    Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                    ArrayList<String> usersChatUpdated = gson.fromJson(response1.getResponseMessage() , listType);

                    if(usersChatUpdated.size() != 0) {

                        ObservableList<String> arrUsers = usersListView.getItems();

                        usersChatUpdated.forEach(item -> {
                            if (CurrentUserInfo.currentChat.equals(item)) {
                                Platform.runLater(() -> {
                                    try {
                                        UpdateChatForUser(item);
                                    } catch (IOException e) {
                                        // TODO
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });

                        // TODO: доделать обновление для других пользователей
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO
                        e.printStackTrace();
                    }
                }while(true);
            }
        };

        logger.info("Thread bound");

        CurrentUserInfo.ourThread = new Thread(task);
        CurrentUserInfo.ourThread.start();
        logger.info("Thread started");
    }

    @Override
    public void UpdateChatForUser(String login) throws IOException {
        logger.info("Sending 'updateChatForUser' request to server");
        StringBuffer url = new StringBuffer();
        url.append("http://localhost:8080/getChat?senderLogin=");
        url.append(CurrentUserInfo.getCurrentUser().getLogin());
        url.append("&senderKey=");
        url.append(CurrentUserInfo.getCurrentKey());
        url.append("&companionLogin=");
        url.append(login);

        URL obj = new URL(url.toString());
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        logger.info("Request was sent");
        Gson gson = new Gson();

        AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);

        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        ArrayList<Message> messages = gson.fromJson(response1.getResponseMessage() , listType);

        chatListView.getItems().clear();

        for(Message msg : messages){
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(msg.getDate().getTime());
            chatListView.getItems().add(dateFormatted + " " + msg.getSender() + " : " + msg.getMessage());
        }
        chatListView.refresh();
        CurrentUserInfo.currentChat = login;

        int index = chatListView.getItems().size() - 1;
        chatListView.scrollTo(index);
    }

    @Override
    public void loadUserChats() throws IOException {
            logger.info("Request 'loaduserchat' configuration");
            StringBuffer url = new StringBuffer();
            url.append("http://localhost:8080/GetUserChats?login=");
            url.append(CurrentUserInfo.getCurrentUser().getLogin());
            url.append("&key=");
            url.append(CurrentUserInfo.getCurrentKey());

            URL obj = new URL(url.toString());
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            logger.info("Request was sent");
            Gson gson = new Gson();

            AuthorizationResponse response1 = gson.fromJson(response.toString(), AuthorizationResponse.class);

            Type listType = new TypeToken<Set<String>>(){}.getType();
            Set<String> currentUsersChat = gson.fromJson(response1.getResponseMessage() , listType);
            currentUsersChat.remove(CurrentUserInfo.getCurrentUser().getLogin());
            usersListView.getItems().addAll(currentUsersChat);
            usersListView.refresh();

    }

    @Override
    public void logOut() {
        logger.info("Logout command");
        CurrentUserInfo.LogOut();
        CurrentUserInfo.ourThread.stop();
        logger.info("Thread was stopped");

        Stage stageToClose = (Stage) logoutButton.getScene().getWindow();
        stageToClose.close();

        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logIn.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStage.setScene(new Scene(root, 620, 680));
        mainStage.show();
        logger.info("Opened logIn.fxml");
    }
}