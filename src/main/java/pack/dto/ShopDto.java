package pack.dto;

import java.util.List;

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
public class ShopDto {
	private ProductDto product;
	private ReviewDto myreviews;//
	private List<ReviewDto> reviews; // 상품별 리뷰 나열하기
	private List<ProductDto> mybuyProducts;
	
	private OrderDto myorder;
	private List<OrderProductDto> orderproducts;
	private List<OrderDto> orders;
	
	/* 주문 내역 출력을 위해 추가한 부분 */
	private List<OrderDto> orderList;
	private List<ProductDto> productList;
	
	/* 주문 상세 출력을 위해 추가한 부분 */
	private OrderDto orderInfo;
	private List<OrderProductDto> orderProductList;
	
	private int totalPages, currentPage;
	private Long totalElements;
	
//	private List<Integer> orderProductNoList;
//	private List<Integer> productNoList;
	
	
}
