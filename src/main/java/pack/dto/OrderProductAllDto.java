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
public class OrderProductAllDto {
	
	private int userNo;
    private double totalAmount; // 주문 총 금액
    private Integer couponNo;
    private List<OrderItemDTO> items; // 상품 리스트
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO { // static으로 선언하여 외부에서 접근 가능하게 수정

        private int productNo; // 상품 번호
        private int quantity; // 수량
        private double resultPrice; // 할인된 가격
    }
}
