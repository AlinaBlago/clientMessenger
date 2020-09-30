package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentUser {
    private static User currentUser;
    public static Thread ourThread ;
    public static String currentChat;
    private static String token;
    private static String username;
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

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CurrentUser.token = token;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }
}
