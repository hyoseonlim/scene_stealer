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
public class CartDto {
    // 장바구니 전체를 관리하기 위한 Dto
    
    private List<CartItemDto> items; // 장바구니에 담긴 상품 리스트
    private double totalPrice; // 장바구니의 총 가격

    // 장바구니에 상품을 추가하는 메서드
    public void addItem(CartItemDto item) {
        item.calculateTotalPrice(); // 총 가격 계산
        this.items.add(item);
        this.totalPrice += item.getTotalPrice(); // 장바구니의 총 가격 업데이트
    }

    // 장바구니에서 상품을 제거하는 메서드
    public void removeItem(CartItemDto item) {
        this.items.remove(item);
        this.totalPrice -= item.getTotalPrice(); // 장바구니의 총 가격 업데이트
    }
}
