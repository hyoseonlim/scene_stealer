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
	
	public static Product toEntity(ProductDto dto) {
    	return Product.builder()
    			.no(dto.getNo())
    			.name(dto.getName())
    			.price(dto.getPrice())
    			.contents(dto.getContents())
    			.date(dto.getDate())
    			.category(dto.getCategory())
    			.pic(dto.getPic())
    			.stock(dto.getStock())
    			.discountRate(dto.getDiscountRate())
    			.score(dto.getScore())
    			.build();
    }

}
