package response;

import java.util.List;

public class FindUserResponse {
    List<String> usernames;

    public FindUserResponse() {
    }

    public FindUserResponse(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
