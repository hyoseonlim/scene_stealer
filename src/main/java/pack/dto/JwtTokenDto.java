package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class JwtTokenDto {
	private String grantType;	// JWT에 대한 인증 타입
	private String accessToken;	// 인증된 사용자가 특정 리소스에 접근할 때 사용되는 토큰
	private String refreshToken;	// Access Token의 갱신을 위해 사용되는 토큰
}
