package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface ApplicationController extends Initializable {

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    void usersListViewChanged(ObservableValue<? extends String> observable, String oldValue, String newValue);

    void send(ActionEvent event);

    void findUser(ActionEvent event);

    void setCurrentUserNameToWindow();

    void bindThreadCheckNewMessages();

    void updateChatForUser(String login) throws IOException;

    void loadUserChats() throws IOException;

    void logOut(ActionEvent event);
}
