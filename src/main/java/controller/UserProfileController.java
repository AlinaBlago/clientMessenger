package controller;

import exceptions.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UserProfileController extends Initializable {
    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    void changePassword(ActionEvent event);
    void changeLogin(ActionEvent event) throws ValidationException;
    void delete(ActionEvent event);
}
