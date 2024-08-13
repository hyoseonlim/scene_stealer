package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Item;
import pack.entity.Product;
import pack.entity.Style;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
	  private Integer no;
	  private String pic;  // URL or file path
	  private Style style;
	  private Product product;
	  
	  public static Item toEntity(ItemDto dto) {
			return Item.builder()
					.no(dto.getNo())
	    			.pic(dto.getPic())
	    			.style(dto.getStyle())
	    			.product(dto.getProduct())
	    			.build();
		}

}
