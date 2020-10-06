package response;

import massage.Message;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class MessageResponse {
    private Long chatId;
    private String senderLogin;
    private Date date;
    private String message;

    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public Date getDate() {
        return date;
    }
}
