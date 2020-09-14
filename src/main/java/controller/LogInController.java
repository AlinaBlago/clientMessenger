package controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface LogInController extends Initializable {

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



    @FXML
    TextField loginField = new TextField();

    @FXML
    Button loginButton = new Button();

    @FXML
    Button signUpButton = new Button();

    @FXML
    PasswordField passwordField = new PasswordField();

    Logger logger = LoggerFactory.getLogger(LogInController.class);

    @FXML
    void initialize();

}
