package response;

public class LoginResponse {
    private String login;
    private String password;

    public static LoginResponse fromMessage(LoginResponse response) {
        return new LoginResponse(
                response.getLogin(),
                response.getPassword()
        );
    }

    LoginResponse() {}

    public LoginResponse(String login, String password) {
        this.login = login;
        this.password = password;
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
}
