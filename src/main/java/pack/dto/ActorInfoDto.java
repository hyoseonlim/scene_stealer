// 작품의 등장인물 웹 스크래핑 & 일반 조회 시 사용됨
package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorInfoDto {
	private int no; // Character PK임. 스타일 조회 시 필요
	
	private String actor;
	private String character;
	private String pic;

}
