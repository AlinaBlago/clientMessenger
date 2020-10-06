package controller.impl;

import controller.ApplicationController;
import data.CurrentUser;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.AddChatRequest;
import request.GetChatRequest;
import request.SendMessageRequest;
import response.ChatResponse;
import response.MessageResponse;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationControllerImpl implements ApplicationController {

    @FXML
    Label currentUserNameLabel;

    @FXML
    Button logoutButton;

    @FXML
    Button sendButton;

    @FXML
    ListView<String> usersListView;

    @FXML
    ListView<String> chatListView;

    @FXML
    TextField sendMessageField;

    @FXML
    TextField findUserLogin;

    @FXML
    Button findUserButton;

    @FXML
    Button profileButton;

    private String selectedChatLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(this::logOut);

        findUserButton.setOnAction(this::findUser);

        sendButton.setOnAction(this::send);

        profileButton.setOnAction(this::onUserProfileClick);

        usersListView.getSelectionModel().selectedItemProperty().addListener(this::usersListViewChanged);

        bindThreadCheckNewMessages();

        try {
            loadUserChats();
        } catch (IOException e) {
            logger.info("Chat doesnt loaded");
            e.printStackTrace();
        }

        setCurrentUserNameToWindow();
    }

    @FXML
    @Override
    public void usersListViewChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        chatListView.getItems().clear();
        chatListView.refresh();

        this.selectedChatLogin = newValue;

        try {
            updateChatForUser(newValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    @Override
    public void send(ActionEvent event) {
        if(!selectedChatLogin.isBlank()){
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setReceiver(usersListView.getSelectionModel().getSelectedItem());
            sendMessageRequest.setMessage(sendMessageField.getText());

            ResponseEntity<String> answer = ServerConnectionProvider.getInstance().sendMessage(sendMessageRequest);

            if (answer.getStatusCode().is2xxSuccessful()) {
                sendMessageField.clear();
                chatListView.getItems().add(sendMessageField.getText());
                chatListView.refresh();
//                chatListView.getItems().add(answer.getBody().getMessage());
//                chatListView.refresh();
            } else{
                logger.warn("Response not 200 from server: " + answer.getStatusCode());
                DialogProvider.ShowDialog("ERROR", "Message doesn't sent", Alert.AlertType.ERROR);
            }
        }
    }

    //TODO
    @FXML
    @Override
    public void findUser(ActionEvent event) {
        if (findUserLogin.getText().length() == 0){
            return;
        }
        try {
            logger.info("Start send 'findUser' to server");

            AddChatRequest request = new AddChatRequest();
            request.setUsername(findUserLogin.getText());
            ResponseEntity<ChatResponse> answer = ServerConnectionProvider.getInstance().addChat(request);
            logger.info("Request sent");

            if (answer.getStatusCode().is2xxSuccessful()) {
                logger.info("Response 0 from server");
                usersListView.getItems().add(findUserLogin.getText());
                findUserLogin.clear();
            } else {
                logger.warn("Response not 0 from server: " + answer.getStatusCode());
                DialogProvider.ShowDialog("ERROR", "User not found", Alert.AlertType.ERROR);

            }
        } catch (Exception e){
            logger.warn(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCurrentUserNameToWindow() {
        String text = "Вы вошли под логином: " + CurrentUser.getUsername();
        currentUserNameLabel.setText(text);
    }

    @Override
    public void bindThreadCheckNewMessages() {
//        Task task = new Task() {
//            @Override
//            protected Void call() throws IOException {
//                do {
//                    List<ServerArgument> argumentsList = new ArrayList<>();
//                    argumentsList.add(new ServerArgument("senderLogin", CurrentUser.getCurrentUser().getLogin()));
//
//                    ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("haveNewMessages", argumentsList, RequestType.GET);
//
//                    Gson gson = new Gson();
//
//                    if (Objects.equals(answer.getBody(), 0)) {
//                        Type listType = new TypeToken<Set<String>>() {
//                        }.getType();
//                        //TODO
//                        Set<String> users = gson.fromJson(String.valueOf(answer.getStatusCode()), listType);
//                    }
//
//                    Type listType = new TypeToken<ArrayList<String>>() {
//                    }.getType();
//                    //TODO
//                    ArrayList<String> usersChatUpdated = gson.fromJson(String.valueOf(answer.getStatusCode()), listType);
//
//                    if (usersChatUpdated.size() != 0) {
//                        ObservableList<String> arrUsers = usersListView.getItems();
//
//                        usersChatUpdated.forEach(item -> {
//                            if (CurrentUser.currentChat.equals(item)) {
//                                Platform.runLater(() -> {
//                                    try {
//                                        updateChatForUser(item);
//                                    } catch (IOException e) {
//                                        logger.warn("Chat doesn't update");
//                                        e.printStackTrace();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                        // TODO: доделать обновление для других пользователей
//
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            logger.warn("Error in Thread");
//                            e.printStackTrace();
//                        }
//
//                } while(true) ;
//            }
//        };
//        logger.info("Thread bound");
//        CurrentUser.ourThread = new Thread(task);
//        CurrentUser.ourThread.start();
//        logger.info("Thread started");
    }

    @Override
    public void updateChatForUser(String login) throws IOException {
        logger.info("Sending 'updateChatForUser' request to server");

        GetChatRequest request = new GetChatRequest();
        request.setUser(usersListView.getSelectionModel().getSelectedItem());

        ResponseEntity<List<MessageResponse>> answer = ServerConnectionProvider.getInstance().getChat(request);

        logger.info("Request was sent");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful");
//            usersListView.getItems().add(findUserLogin.getText());
//            usersListView.refresh();
//            chatListView.getItems().clear();

        for(MessageResponse msg : answer.getBody()){
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(msg.getDate().getTime());
            chatListView.getItems().add(dateFormatted + " " + msg.getSenderLogin() + " : " + msg.getMessage());
        }
        chatListView.refresh();
        CurrentUser.currentChat = login;

        int index = chatListView.getItems().size() - 1;
        chatListView.scrollTo(index);
        } else {
            logger.warn("Error: " + answer.getStatusCode());
            DialogProvider.ShowDialog("ERROR", "Loading error", Alert.AlertType.ERROR);

        }
    }

    @Override
    public void loadUserChats () throws IOException {
        logger.info("Request 'loaduserchat' configuration");
        ResponseEntity<List<ChatResponse>> answer = ServerConnectionProvider.getInstance().getUserChats();
        logger.info("Request was sent");

        List<String> chats = new ArrayList();

        answer.getBody().forEach(chatResponse ->
        {
            if(!chatResponse.getLoginFirst().equals(CurrentUser.getUsername())){
                chats.add(chatResponse.getLoginFirst());
            }else{
                chats.add(chatResponse.getLoginSecond());
            }
        });

        usersListView.getItems().addAll(chats);
        usersListView.refresh();
    }

    @FXML
    private void onUserProfileClick(ActionEvent event){

        Stage userProfile = new Stage();
        Parent userProfileSceneRoot = null;
        try {
            userProfileSceneRoot = FXMLLoader.load(ApplicationControllerImpl.this.getClass().getResource("/userProfile2.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userProfile.setScene(new Scene(userProfileSceneRoot, 620, 700));
        userProfile.show();
    }

    @FXML
    @Override
    public void logOut(ActionEvent event) {
        logger.info("Logout command");
        CurrentUser.logOut();

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
