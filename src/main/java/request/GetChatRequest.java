package request;

public class GetChatRequest {
    private String user;

    public GetChatRequest() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GetChatRequest{" +
                "user='" + user + '\'' +
                '}';
    }
}
