package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SubmitChangePassword extends Initializable {
    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    void changePassword(ActionEvent event);
}
