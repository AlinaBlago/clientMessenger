package controller.impl;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import controller.ApplicationController;
import data.CurrentUser;
import data.SimpleEventHandler;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import org.springframework.http.ResponseEntity;
import providers.DialogProvider;
import providers.ServerConnectionProvider;
import request.UserRequest;
import request.SendMessageRequest;
import response.ChatResponse;
import response.FindUserResponse;
import response.MessageResponse;
import runner.Main;
import util.FxUtilTest;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    TextField txtFindLogin;

    @FXML
    ComboBox<String> findUserComboBox;

    @FXML
    Button findUserButton;

    @FXML
    Button profileButton;

    private String selectedChatLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindEventActions();
        bindThreadCheckNewMessages();

        try {
            loadUserChats();
        } catch (IOException e) {
            logger.info("Chat doesnt loaded");
            e.printStackTrace();
        }

        setCurrentUserNameToWindow();
    }

    private void bindEventActions(){
        logoutButton.setOnAction(this::logOut);

        findUserButton.setOnAction(this::findUser);

        sendButton.setOnAction(this::send);

        profileButton.setOnAction(this::onUserProfileClick);

        usersListView.getSelectionModel().selectedItemProperty().addListener(this::usersListViewChanged);

        txtFindLogin.textProperty().addListener(this::textChanged);
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

            ResponseEntity<MessageResponse> answer = ServerConnectionProvider.getInstance().sendMessage(sendMessageRequest);
            sendMessageField.clear();

            if (answer.getStatusCode().is2xxSuccessful()) {
                DateFormat formatter = new SimpleDateFormat("HH:mm");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateFormatted = formatter.format(Objects.requireNonNull(answer.getBody()).getDate().getTime());
                chatListView.getItems().add(dateFormatted + " " + answer.getBody().getSenderLogin() + " : " + answer.getBody().getMessage());
                chatListView.refresh();
            } else{
                logger.warn("Response not 200 from server: " + answer.getStatusCode());
                DialogProvider.showDialog("ERROR", "Message doesn't sent", Alert.AlertType.ERROR , false);
            }
        }
    }

    @FXML
    public void textChanged(ObservableValue<? extends String> observable,
                            String oldValue, String newValue){
        if (newValue.length() >= 2) {
            findUserComboBox.getItems().clear();
            UserRequest request = new UserRequest();
            request.setUsername(newValue);
            ResponseEntity<FindUserResponse> answer = ServerConnectionProvider.getInstance().findUser(request);
            logger.info("Request sent");

            if (answer.getStatusCode().is2xxSuccessful()) {
                logger.info("Response 200 from server");
                Objects.requireNonNull(answer.getBody()).getUsernames().forEach(username ->{
                    findUserComboBox.getItems().addAll(username);
                });

            } else {
                logger.warn("Response not 200 from server: " + answer.getStatusCode());
                DialogProvider.showDialog("ERROR", "User not found", Alert.AlertType.ERROR, false);

            }
        }
    }

    @FXML
    @Override
    public void findUser(ActionEvent event) {
        if (!findUserComboBox.getSelectionModel().getSelectedItem().isBlank())
        {
            String login = findUserComboBox.getSelectionModel().getSelectedItem();
            logger.info("Start send 'findUser' to server");

            FxUtilTest.getComboBoxValue(findUserComboBox);
            UserRequest request = new UserRequest();
            request.setUsername(login);
            ResponseEntity<ChatResponse> answer = ServerConnectionProvider.getInstance().addChat(request);
            logger.info("Request sent");

            if (answer.getStatusCode().is2xxSuccessful()) {
                logger.info("Response 200 from server");
                usersListView.getItems().add(login);
                usersListView.refresh();
            } else {
                logger.warn("Response not 200 from server: " + answer.getStatusCode());
                DialogProvider.showDialog("ERROR", "User not found", Alert.AlertType.ERROR , false);

            }
        }
    }

    @Override
    public void setCurrentUserNameToWindow() {
        String text = "Вы вошли под логином: " + CurrentUser.getUsername();
        currentUserNameLabel.setText(text);
    }

    @Override
    public void bindThreadCheckNewMessages() {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                SimpleEventHandler simleEvent = new SimpleEventHandler();
                simleEvent.setRetrieveMessage(retrieveMessage());

                EventHandler eventHandler = simleEvent;

                String url = "https://xettlena.herokuapp.com/users/me/newMessages";
                EventSource.Builder builder = new EventSource.Builder(eventHandler, HttpUrl.get(url));


                Headers headers = new Headers.Builder()
                        .add("Authorization", CurrentUser.getAuthToken())
                        .build();

                builder.headers(headers);
                try (EventSource eventSource = builder.build()) {
                    eventSource.start();

                    TimeUnit.MINUTES.sleep(24 * 60);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        Main.t1 = new Thread(task);
        Main.t1.start();
    }

    public RetrieveMessage retrieveMessage(){
        return new RetrieveMessage() {
            @Override
            public void retrieve(List<MessageResponse> responses) {
                responses.forEach(response -> {
                    if(selectedChatLogin.equals(response.getSenderLogin())){
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String dateFormatted = formatter.format(response.getDate().getTime());
                        chatListView.getItems().add(dateFormatted + " " + response.getSenderLogin() + " : " + response.getMessage());
                        chatListView.refresh();
                    }
                });
            }
        };
    }

    @Override
    public void updateChatForUser(String login) throws IOException {
        logger.info("Sending 'updateChatForUser' request to server");

        UserRequest request = new UserRequest();
        request.setUsername(usersListView.getSelectionModel().getSelectedItem());

        ResponseEntity<List<MessageResponse>> answer = ServerConnectionProvider.getInstance().getChat(request);

        logger.info("Request was sent");

        if (answer.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful");

        for(MessageResponse msg : Objects.requireNonNull(answer.getBody())){
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
            DialogProvider.showDialog("ERROR", "Loading error", Alert.AlertType.ERROR , false);
        }
    }

    @Override
    public void loadUserChats () throws IOException {
        logger.info("Request 'loaduserchat' configuration");
        ResponseEntity<List<ChatResponse>> answer = ServerConnectionProvider.getInstance().getUserChats();
        logger.info("Request was sent");

        List<String> chats = new ArrayList();

        Objects.requireNonNull(answer.getBody()).forEach(chatResponse ->
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


        Main.StopThread();

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


