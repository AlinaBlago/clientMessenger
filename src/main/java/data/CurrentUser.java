package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentUser {
    private static User currentUser;
    private static int currentKey;
    public static Thread ourThread ;
    public static String currentChat;
    private static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentKey() {
        return currentKey;
    }

    public static void init(User user , int key){
        currentUser = user;
        currentKey = key;
    }

    public static void logOut(){
        currentKey = 0;
        currentUser = null;
    }
}
