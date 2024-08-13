package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Alert;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {
	private Integer no;
	
	private User user;
	private String category;
	private String content;
	private java.util.Date date;

	private Boolean isRead;
	
	public static Alert toEntity(AlertDto dto) {
		return Alert.builder()
				.no(dto.getNo())
    			.user(dto.getUser())
    			.category(dto.getCategory())
    			.content(dto.getContent())
    			.date(dto.getDate())
    			.isRead(dto.getIsRead())
    			.build();
	}
}
