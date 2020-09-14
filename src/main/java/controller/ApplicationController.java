package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface ApplicationController extends Initializable {

    @FXML
    Label currentUserNameLabel = new Label();

    @FXML
    Button logoutButton = new Button();

    @FXML
    Button sendButton = new Button();

    @FXML
    ListView<String> usersListView = new ListView<>();

    @FXML
    ListView<String> chatListView = new ListView<>();

    @FXML
    TextField sendMessageField = new TextField();

    @FXML
    TextField findUserLogin = new TextField();

    @FXML
    Button findUserButton = new Button();

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    void usersListViewChanged(String newValue);

    void send();

    void findUser();

    void setCurrentUserNameToWindow();

    void bindThreadCheckNewMessages();

    void UpdateChatForUser(String login) throws IOException;

    void loadUserChats() throws IOException;

    void logOut();
}
