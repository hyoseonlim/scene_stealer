package pack.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Order;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	  private Integer no;
	  
	  private UserDto user;
	  private String state;
	  
	  private java.util.Date date;

	  private Integer price;
	  
	  private Integer userNo;
	 private String userId;
	 
	 private List<Integer> productNoList;
	  
	  private List<OrderProductDto> orderProducts;
	  

		public static Order toEntity(OrderDto dto) {
			return Order.builder()
					.no(dto.getNo())
	    			.user(UserDto.toEntity(dto.getUser()))
	    			.state(dto.getState())
	    			.date(dto.getDate())
	    			.price(dto.getPrice())
	    			.orderProducts(dto.getOrderProducts().stream().map(OrderProductDto::toEntity).collect(Collectors.toList()))
	    			.build();
		}

}
