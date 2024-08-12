package pack.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.OrderProduct;
import pack.entity.Product;
import pack.entity.Reviews;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private Integer no;

	private String name;
	private Integer price;
	private String contents;

	private java.util.Date date;

	private String category;

	private String pic; // URL or file path

	private Integer stock;
	private Integer discountRate;
	private java.math.BigDecimal score;

	private List<Reviews> reviews = new ArrayList<>();

	private List<OrderProduct> orderProducts = new ArrayList<>();
	
	 public static ProductDto toDto(Product entity) {
	    	return ProductDto.builder()
	    			.no(entity.getNo())
	    			.name(entity.getName())
	    			.price(entity.getPrice())
	    			.contents(entity.getContents())
	    			.date(entity.getDate())
	    			.category(entity.getCategory())
	    			.pic(entity.getPic())
	    			.stock(entity.getStock())
	    			.discountRate(entity.getDiscountRate())
	    			.score(entity.getScore())
	    			.build();
	    }

}
