package controller.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import data.CurrentUser;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import massage.Message;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.AddChatRequest;
import request.GetChatRequest;
import request.SendMessageRequest;
import response.ChatResponse;
import response.MessageResponse;

import java.io.IOException;
import java.lang.reflect.Type;
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

    private String selectedChatLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(this::logOut);

        findUserButton.setOnAction(this::findUser);

        sendButton.setOnAction(this::send);

        usersListView.getSelectionModel().selectedItemProperty().addListener(this::usersListViewChanged);

        bindThreadCheckNewMessages();

        try {
            loadUserChats();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        setCurrentUserNameToWindow();
    }

    @FXML
    @Override
    public void usersListViewChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        //TODO: GET /me/messages (get all messages with that human)

        chatListView.getItems().clear();
        chatListView.refresh();
        this.selectedChatLogin = newValue;

        // сделать этот метод если рабоатет отправка , и сделан метод GET /me/messages
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
                chatListView.getItems().add(sendMessageField.getText());
                chatListView.refresh();
//                chatListView.getItems().add(answer.getBody().getMessage());
//                chatListView.refresh();
            } else{
                logger.warn("Response not 0 from server: " + answer.getStatusCode());
                DialogProvider.ShowDialog("ERROR", "Message doesn't sent", Alert.AlertType.ERROR);
            }

        }
//        if (usersListView.getSelectionModel().isEmpty()){
//            logger.info("Send function call: user is empty");
//            return;
//        }
//        else{
//            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
//            if(sendMessageField.getText().length() > 0){
//                boolean isExistsOnlyOfSpace = true;
//
//                for(Character symbol : sendMessageField.getText().toCharArray()){
//                    if(!symbol.equals(' ')){
//                        isExistsOnlyOfSpace = false;
//                        break;
//                    }
//                }
//                if(isExistsOnlyOfSpace){
//                    logger.info("Entered message text consist only of space");
//                    return;
//                } else {
//                    try {
//                        logger.info("Staring send 'sendMessage' to server");
//
//                        List<ServerArgument> argumentsList = new ArrayList<>();
//                        argumentsList.add(new ServerArgument("senderLogin", CurrentUser.getCurrentUser().getLogin()));
//                        argumentsList.add(new ServerArgument("receiverLogin", usersListView.getSelectionModel().getSelectedItem()));
//                        argumentsList.add(new ServerArgument("message", sendMessageField.getText().replaceAll(" " , "%20")));
//
//                        ResponseEntity<Integer> answer = ServerConnectionProvider.getInstance().loginRequest("sendMessage" ,argumentsList, RequestType.GET);
//                        String mesg = sendMessageField.getText().replaceAll(" ", "%20");
//
//                        logger.info("Request 'sendMessage' sent" );
//
//                        DateFormat formatter = new SimpleDateFormat("HH:mm");
//                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//                        String dateFormatted = formatter.format(System.currentTimeMillis());
//                        chatListView.getItems().add(dateFormatted + " " + CurrentUser.getCurrentUser().getLogin() + ": " + sendMessageField.getText());
//
//                        int index = chatListView.getItems().size() - 1;
//                        chatListView.scrollTo(index);
//
//                        sendMessageField.clear();
//
//                    } catch (Exception e){
//                        logger.warn(e.getMessage());
//                        System.out.println(e.getMessage());
//                    }
//                }
//            } else {
//                return;
//            }
//            logger.info("Entered message text less than 0 symbols");
//        }
    }

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
                usersListView.refresh();
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

        ResponseEntity<List<Message>> answer = ServerConnectionProvider.getInstance().getChat(request);

        logger.info("Request was sent");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful");
            usersListView.getItems().add(findUserLogin.getText());
            usersListView.refresh();
            chatListView.getItems().clear();

        for(Message msg : answer.getBody()){
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(msg.getDate().getTime());
            chatListView.getItems().add(dateFormatted + " " + msg.getSender() + " : " + msg.getMessage());
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
