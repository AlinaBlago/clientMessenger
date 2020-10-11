package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface LogInController extends Initializable {
    Logger logger = LoggerFactory.getLogger(LogInController.class);

    void login(ActionEvent event);

    void signUp(ActionEvent event);
}
