package request;

import javax.validation.constraints.NotBlank;

public class SendChangePasswordTokenRequest {
    @NotBlank
    private String username;

    SendChangePasswordTokenRequest() {}

    public SendChangePasswordTokenRequest(@NotBlank String username) {
        this.username = username;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
