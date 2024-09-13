// 관리자의 아이템 관리를 위한 DTO
// 각 아이템과 연결된 n개의 스타일 정보
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
public class StyleInfoDto {
    private Integer no; // StyleItem - no    
    private StyleDto style;
    private ActorInfoDto actorInfo;
    private String showTitle; // Show - title
}
