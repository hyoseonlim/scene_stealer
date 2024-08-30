package pack.websocket;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate msgt;

    @Autowired
    private ChatRepository crps;

    @Autowired
    private ChatUserRepository curps;

    private static final String ADMIN_ID = "1"; // 관리자 ID 고정

    @MessageMapping("/chat/message")
    public void message(ChatDto dto) {
    	Optional<ChatUser> chatUser = curps.findById(dto.getChatNo());
    	
    	System.out.println(dto.getDate());
    	System.out.println(dto.getNo());
    	System.out.println(dto.getContent());
    	System.out.println(dto.getChatNo());
    	
//    	if(!chatUser.isPresent()) {
//    		ChatUser createChatNo = ChatUser.builder().
//    	} 
//    	
//        // 메시지를 DB에 저장
//        Chat chat = Chat.builder()
//            .closeChat(dto.isCloseChat())
//            .content(dto.getContent())
//            .sendAdmin(dto.isSendAdmin())
//            .date(dto.getDate())
//            .build();
//        crps.save(chat);
//
//        // 채팅방 ID 생성 (관리자 ID와 유저 ID를 조합)
//        String chatRoomId = createChatRoomId(chatUser.getNo());
//
//        // 메시지를 채팅방 구독자에게 전송
//        msgt.convertAndSend("/sub/chat/room/" + chatRoomId, Chat.toDto(chat));
    }

    private String createChatRoomId(Integer chatUserNo) {
        // 관리자의 ID와 ChatUser ID를 조합하여 고유한 채팅방 ID 생성
        return ADMIN_ID + "_" + chatUserNo;
    }
}
