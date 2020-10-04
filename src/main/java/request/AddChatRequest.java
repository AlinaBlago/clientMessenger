package request;

public class AddChatRequest {
    private String username;

    public AddChatRequest() {
    }

    public AddChatRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AddChatRequest{" +
                "login='" + username + '\'' +
                '}';
    }
}
