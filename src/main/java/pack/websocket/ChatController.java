package pack.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
	
	// 채팅 구현을 위한 컨트롤러
	// 일단 SpringSecurity를 사용한 로그인 세션이 구현 완료된 후
	// 테스트 및 Dto 확정

    @Autowired
    private SimpMessagingTemplate msgt;
    // SimpMessagingTemplate : 웹 소켓 메시징을 위한 템플릿 제공
    // 메시지를 전송하는 메소드 제공

    @MessageMapping("/chat/message")
    // 클라이언트가 /chat/message 경로로 메시지 전송하면 이 메소드가 실행됨
    // MessageMapping : 클라이언트의 웹 소켓 메시지를 메소드와 매핑해 주는 어노테이션
    public void message(ChatDto dto) {
    	msgt.convertAndSend("/sub/chat/room/100", dto);
    	// convertAndSend : 경로, 전송할 것
    	// 여기서는 /sub/chat/room/100 경로로 dto를 전송하는 것인데
    	// 추후 세션 작업 후 경로를 관리자 세션 경로로 변경해 주면 됨
    }
}
