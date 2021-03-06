package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentUser {
    private static User currentUser;
    public static String currentChat;
    private static String changePasswordToken;
    private static String changeEmailToken;
    private static String authToken;
    private static String username;
    private static String email;
    private static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void init(User user){
        currentUser = user;
    }

    public static void logOut(){
        currentUser = null;
    }

    public static String getChangePasswordToken() {
        return changePasswordToken;
    }

    public static void setChangePasswordToken(String changePasswordToken) {
        CurrentUser.changePasswordToken = changePasswordToken;
    }

    public static String getChangeEmailToken() {
        return changeEmailToken;
    }

    public static void setChangeEmailToken(String changeEmailToken) {
        CurrentUser.changeEmailToken = changeEmailToken;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        CurrentUser.authToken = authToken;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }
}
