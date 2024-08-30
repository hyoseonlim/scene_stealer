package pack.websocket;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.ShowActorDto;
import pack.entity.ShowActor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private boolean closeChat;

	private String content;

	private boolean sendAdmin;

	@ManyToOne
	@JoinColumn(name = "chat_no") // chatuser와 매핑될 컬럼 이름을 명확하게 지정
	private ChatUser chatuser;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date date;

	public static ChatDto toDto(Chat entity) {
		return ChatDto.builder()
				.no(entity.getNo())
				.closeChat(entity.isCloseChat())
				.content(entity.getContent())
				.sendAdmin(entity.isSendAdmin())
				.chatNo(entity.getChatuser().getNo())
				.date(entity.getDate())
				.build();
	}

}
