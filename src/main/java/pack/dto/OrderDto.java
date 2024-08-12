package pack.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Coupon;
import pack.entity.Order;
import pack.entity.OrderProduct;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	  private Integer no;
	  
	  private User user;
	  private String state;
	  
	  private java.util.Date date;

	  private Integer price;
	  
	  private List<OrderProduct> orderProducts = new ArrayList<>();

		public static Order toEntity(OrderDto dto) {
			return Order.builder()
					.no(dto.getNo())
	    			.user(dto.getUser())
	    			.state(dto.getState())
	    			.date(dto.getDate())
	    			.price(dto.getPrice())
	    			.orderProducts(dto.getOrderProducts())
	    			.build();
		}

}
