package pack.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ChatService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatUserRepository curps;

    // 새로운 채팅방 생성
    public void createChatRoom(ChatUser chatRoom) {
    	curps.save(chatRoom);
        updateChatRooms();
    }

    // 채팅방 종료
    public void closeChatRoom(int chatRoomId) {
    	ChatUser chatRoom = curps.findById(chatRoomId).orElseThrow();
        chatRoom.setCloseChat(true);
        curps.save(chatRoom);
        updateChatRooms();
    }

    // 클라이언트에 채팅방 목록을 전송
    public void updateChatRooms() {
        List<ChatUser> chatRooms = curps.findAll();
        messagingTemplate.convertAndSend("/sub/chat/room/update", chatRooms);
    }
}
