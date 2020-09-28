package request;

import javax.validation.constraints.NotBlank;

public class SignupRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    private String password;

    public SignupRequest() {}

    public SignupRequest(@NotBlank String name, @NotBlank String login, @NotBlank String password) {
        this.name = name;
        this.login = login;
        this.password = password;
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
}

