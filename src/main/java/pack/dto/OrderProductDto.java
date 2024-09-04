package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.OrderProduct;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
	    private Integer no;

	    private OrderDto order;

	    private ProductDto product;

	    private Integer price;
	    private Integer quantity;
	    
	    private Integer orderNo, productNo;
	    
	    private Boolean isReview;
	    
		public static OrderProduct toEntity (OrderProductDto dto) {
	    	return OrderProduct.builder()
	    			.no(dto.getNo())
	    			.order(OrderDto.toEntity(dto.getOrder()))
	    			.product(ProductDto.toEntity(dto.getProduct()))
	    			.price(dto.getPrice())
	    			.quantity(dto.getQuantity())
	    			.isReview(dto.getIsReview())
	    			.build();
	    }

}
