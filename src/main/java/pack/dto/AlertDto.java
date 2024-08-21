package pack.dto;

import java.util.List;

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
	
	private UserDto user;
	private String category; 
	private String content;
	private java.util.Date date;
	
	private Integer userNo;

	private Boolean isRead;
	
	private String path;
	
	public static Alert toEntity(AlertDto dto) {
		return Alert.builder()
				.no(dto.getNo())
    			.user(UserDto.toEntity(dto.getUser()))
    			.category(dto.getCategory())
    			.content(dto.getContent())
    			.date(dto.getDate())
    			.isRead(dto.getIsRead())
    			.path(dto.getPath())
    			.build();
	}
}
