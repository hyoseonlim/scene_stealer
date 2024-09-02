package pack.websocket;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.UserDto;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDto {
	private Integer no;
	private UserDto user;
	private Integer userNo;
	private String userName;
	private Integer chatNo;
	private String category;
	private Boolean closeChat;
	private List<Integer> chatsList;
	
	public static ChatUser toEntity(ChatUserDto dto) {
        return ChatUser.builder()
        		.no(dto.getNo())
        		.user(User.builder().no(dto.getUserNo()).build())
        		.chats(null)
        		.category(dto.getCategory())
        		.closeChat(dto.getCloseChat())
        		.build();
    }
}
