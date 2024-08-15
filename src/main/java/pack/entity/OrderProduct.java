package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.OrderProductDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer no;

	    @ManyToOne
	    @JoinColumn(name = "order_no")
	    private Order order;

	    @ManyToOne
	    @JoinColumn(name = "product_no")
	    private Product product;

	    private Integer price;
	    private Integer quantity;
	    
		public static OrderProductDto toDto (OrderProduct entity) {
	    	return OrderProductDto.builder()
	    			.no(entity.getNo())
	    			.order(Order.toDto(entity.getOrder()))
	    			.product(Product.toDto(entity.getProduct()))
	    			.price(entity.getPrice())
	    			.quantity(entity.getQuantity())
	    			.build();
	    }

}
