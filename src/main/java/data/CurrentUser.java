package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentUser {
    private static User currentUser;
    private static String currentKey;
    public static Thread ourThread ;
    public static String currentChat;
    private static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentKey() {
        return currentKey;
    }

    public static void Init(User user , String key){
        currentUser = user;
        currentKey = key;
    }

    public static void LogOut(){
        currentKey = "";
        currentUser = null;
    }
}
