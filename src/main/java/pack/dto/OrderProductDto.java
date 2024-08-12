package pack.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Order;
import pack.entity.OrderProduct;
import pack.entity.Product;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
	    private Integer no;

	    private Order order;

	    private Product product;

	    private Integer price;
	    private Integer quantity;
	    
		public static OrderProduct toDto (OrderProductDto dto) {
	    	return OrderProduct.builder()
	    			.no(dto.getNo())
	    			.order(dto.getOrder())
	    			.product(dto.getProduct())
	    			.price(dto.getPrice())
	    			.quantity(dto.getQuantity())
	    			.build();
	    }

}
