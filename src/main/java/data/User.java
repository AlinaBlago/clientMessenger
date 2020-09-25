package data;

public class User {
    private static int NextID;
    private int ID;
    private String name;
    private String login;
    private String password;
    private boolean isBanned;

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.isBanned = false;
    }

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public static int getNextID() {
        NextID += 1;
        return NextID;
    }

    public static void setNextID(int nextID) {
        NextID = nextID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
