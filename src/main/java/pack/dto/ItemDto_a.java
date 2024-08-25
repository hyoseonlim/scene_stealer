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
public class ItemDto_a {
	private int no; // 아이템 PK
	private int style; // 스타일 PK
	private int product; // 상품 PK
	private String pic; // 아이템 사진
}
