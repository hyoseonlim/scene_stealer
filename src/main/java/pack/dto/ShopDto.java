package pack.dto;

import java.util.List;

import org.springframework.data.domain.Page;

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
	//private List<ReviewDto> reviews; // 상품별 리뷰 나열하기
	private List<ProductDto> mybuyProducts;
	
	// 페이징 처리된 리뷰 리스트 추가
    private Page<ReviewDto> reviews; // 페이징 처리된 리뷰 나열하기
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
	
	// 장바구니 부분
	private long userId;
	
	private CartDto cart; // 장바구니 정보 추가
	
	private UserDto user; // 사용자 정보 추가
	
}
