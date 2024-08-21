package pack.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // MessageType은 메시지의 유형을 정의한 enum 클래스입니다.
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}

