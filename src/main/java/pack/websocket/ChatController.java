//package pack.websocket;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    @Autowired
//    private SimpMessagingTemplate msgt;
//
//    @Autowired
//    private ChatRepository crps;
//
//    @Autowired
//    private ChatUserRepository curps;
//
//    private static final String ADMIN_ID = "0"; // 관리자 ID 고정
//
//    @MessageMapping("/chat/message")
//    public void message(ChatDto dto) {
//        // ChatUser 객체를 가져옴 (해당 유저의 채팅방)
//        ChatUser chatUser = chatUserRepository.findById(dto.getNo())
//            .orElseThrow(() -> new IllegalArgumentException("Invalid ChatUser ID: " + dto.getNo()));
//
//        // 메시지를 DB에 저장
//        Chat chat = Chat.builder()
//            .no(dto.getNo())
//            .closeChat(dto.isCloseChat())
//            .content(dto.getContent())
//            .sendAdmin(dto.isSendAdmin())
//            .date(dto.getDate())
//            .build();
//        chatRepository.save(chat);
//
//        // 채팅방 ID 생성 (관리자 ID와 유저 ID를 조합)
//        String chatRoomId = createChatRoomId(chatUser.getNo());
//
//        // 메시지를 채팅방 구독자에게 전송
//        msgt.convertAndSend("/sub/chat/room/" + chatRoomId, Chat.toDto(chat));
//    }
//
//    private String createChatRoomId(Integer chatUserNo) {
//        // 관리자의 ID와 ChatUser ID를 조합하여 고유한 채팅방 ID 생성
//        return ADMIN_ID + "_" + chatUserNo;
//    }
//}
