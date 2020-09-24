package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface LogUpController extends Initializable {

    Logger logger = LoggerFactory.getLogger(LogUpController.class);

    void onSignUpClick(ActionEvent event);

}
