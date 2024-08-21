package pack.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 사용자가 관리자에게 메시지를 보낼 때
    @MessageMapping("/chat/message")
    public void message(ChatDto dto) {
        // 관리자의 ID로 메시지를 전송 (예: 관리자의 userNo가 100이라고 가정)
        messagingTemplate.convertAndSend("/sub/chat/room/100", dto);
    }
}
