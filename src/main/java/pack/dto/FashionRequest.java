// 작품, 배우, 배역 동시 추가 요청 시 여러 객체를 감싸기 위한 용도
package pack.dto;

import java.util.List;
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
public class FashionRequest {
    private List<ActorScrapDto> actors;
    private ShowDto show;
}