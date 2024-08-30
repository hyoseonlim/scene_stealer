package pack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 비밀번호 재설정 요청에 필요한 정보를 담는 DTO
public class FindPassDto {
	private String email;
	private String id;
	
	public FindPassDto() {
		
	}
	
	public FindPassDto(String email, String id) {
		this.email = email;
		this.id = id;
	}
}
