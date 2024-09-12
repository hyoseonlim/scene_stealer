// 관리자의 아이템 관리를 위한 DTO
// 각 아이템과 연결된 n개의 스타일 정보
package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Actor;
import pack.entity.Style;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StyleInfoDto {
    private Integer no; // StyleItem - no    
    private StyleDto style;
    private String characterName; // Character - name
    private String actorName; // Actor - name
    private String showTitle; // Show - title
}
