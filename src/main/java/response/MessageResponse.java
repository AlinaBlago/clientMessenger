package response;

import massage.Message;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class MessageResponse {
    public MessageResponse(Long chatId, String senderLogin, Date date, String message) {
 //       this.chatId = chatId;
        this.senderLogin = senderLogin;
        this.date = date;
        this.message = message;
    }

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
