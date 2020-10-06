package request;

import javax.validation.constraints.Email;

public class GetTokenForUpdateEmailRequest {
    @Email
    private String email;

    public GetTokenForUpdateEmailRequest() {
    }

    public GetTokenForUpdateEmailRequest(@Email String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
