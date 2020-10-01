package request;

import javax.validation.constraints.NotBlank;

public class GetUserChatsRequest {
    @NotBlank
    private String token;

    public GetUserChatsRequest() {
    }

    public GetUserChatsRequest(@NotBlank String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
