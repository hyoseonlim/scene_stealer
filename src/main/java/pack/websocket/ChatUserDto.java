package pack.websocket;


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
	private String category;
	
	public static ChatUser toEntity(ChatUserDto dto) {
        return ChatUser.builder()
        		.no(dto.getNo())
        		.user(User.builder().no(dto.getUserNo()).build())
        		.category(dto.getCategory())
        		.build();
    }
}
