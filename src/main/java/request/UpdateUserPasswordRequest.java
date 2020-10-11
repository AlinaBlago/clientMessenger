package request;

public class UpdateUserPasswordRequest {
    private String oldPassword;
    private String password;

    public UpdateUserPasswordRequest() {
    }

    public UpdateUserPasswordRequest(String oldPassword, String password) {
        this.oldPassword = oldPassword;
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UpdateUserPasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
