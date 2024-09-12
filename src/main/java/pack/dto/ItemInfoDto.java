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
public class ItemInfoDto {
	private Integer no; // 아이템 PK
	private String name; // 아이템 이름
	private String pic; // 아이템 사진
	private Integer productNo; // 상품 번호
	private String productName; // 상품명
	private String productPic; // 상품 사진
	private List<StyleInfoDto> styleInfos;
}
