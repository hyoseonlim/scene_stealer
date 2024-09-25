package pack.dto;

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
public class CartItemDto {
    private ProductDto product; // 장바구니에 담긴 상품 정보
    private int quantity; // 해당 상품의 수량
    private double totalPrice; // 해당 상품의 총 가격 (상품 가격 * 수량)

    // totalPrice 계산 메서드
    public void calculateTotalPrice() {
        this.totalPrice = this.product.getPrice() * this.quantity;
    }
}
