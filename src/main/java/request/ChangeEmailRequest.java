package request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangeEmailRequest {
    @NotBlank
    private String token;

    @Email
    @NotBlank
    private String email;

    public ChangeEmailRequest() {
    }

    public ChangeEmailRequest(@NotBlank String token, @Email @NotBlank String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
