package pack.websocket;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_chat")
public class ChatUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@ManyToOne
    @JoinColumn(name = "user_no")
	private User user;
	
	private String category;
	
	public static ChatUserDto toDto(ChatUser entity) {
        return ChatUserDto.builder()
        		.no(entity.getNo())
        		.userNo(entity.getUser().getNo())
        		.category(entity.getCategory())
        		.build();
    }
}
