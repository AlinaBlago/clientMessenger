package response;

public class ChatResponse {
    private Long idChat;
    private String loginFirst;
    private String loginSecond;

    public ChatResponse() {
    }

    public ChatResponse(Long idChat, String loginFirst, String loginSecond) {
        this.idChat = idChat;
        this.loginFirst = loginFirst;
        this.loginSecond = loginSecond;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getLoginFirst() {
        return loginFirst;
    }

    public void setLoginFirst(String loginFirst) {
        this.loginFirst = loginFirst;
    }

    public String getLoginSecond() {
        return loginSecond;
    }

    public void setLoginSecond(String loginSecond) {
        this.loginSecond = loginSecond;
    }
}
