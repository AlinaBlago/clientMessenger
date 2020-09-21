package controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    void UpdateChatForUser(String login) throws IOException;

    void loadUserChats() throws IOException;

    void logOut(ActionEvent event);
}
