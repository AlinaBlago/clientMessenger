package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentUser {
    private static User currentUser;
    public static Thread ourThread ;
    public static String currentChat;
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
}
