package request;

import javax.validation.constraints.NotBlank;

public class SendMessageRequest {
    @NotBlank
    private Long chatId;
    @NotBlank
    private String message;

    public SendMessageRequest() {
    }

    public SendMessageRequest(@NotBlank Long chatId, @NotBlank String message) {
        this.chatId = chatId;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "chatId=" + chatId +
                ", message='" + message + '\'' +
                '}';
    }
}
