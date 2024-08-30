package pack.websocket;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.AlertDto_a;
import pack.dto.UserDto;
import pack.entity.Alert;
import pack.entity.User;
import pack.repository.UsersRepository;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

	private Integer no;

	private boolean closeChat;

	private boolean sendCheck;

	private String content;
	
	private boolean sendAdmin;

	private Date date;

	public static Chat toEntity(ChatDto dto) {
		return Chat.builder()
				.no(dto.getNo())
				.closeChat(dto.isCloseChat())
				.content(dto.getContent())
				.sendAdmin(dto.isSendAdmin())
				.date(dto.getDate())
				.build();
	}
}
