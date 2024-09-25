// 작품 정보 조회 시 사용
package pack.dto;

import java.util.ArrayList;

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
public class ShowInfoDto {
	private ShowDto show;
	private ArrayList<ActorInfoDto> actorsInfo;
}
