package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Item;
import pack.entity.Style;
import pack.entity.StyleItem;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StyleItemDto {

	private Integer no, styleNo, itemNo;

	  public static StyleItem toEntity(StyleItemDto dto) {
			return StyleItem.builder()
					.no(dto.getNo())
	    			.style(Style.builder().no(dto.getStyleNo()).build())
	    			.item(Item.builder().no(dto.getItemNo()).build())
	    			.build();
		}

}
