package request;

public class UpdateUserLoginRequest {
    private String username;

    public UpdateUserLoginRequest() {
    }

    public UpdateUserLoginRequest(String username) {
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
        return "UpdateUserRequest{" +
                ", username='" + username + '\'' +
                '}';
    }

}
