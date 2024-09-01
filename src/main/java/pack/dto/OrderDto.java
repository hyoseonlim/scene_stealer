package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

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

    // 주문의 모든 제품 수량 합계 계산
    public int getTotalQuantity() {
        return orderProducts.stream()
                .mapToInt(OrderProductDto::getQuantity)
                .sum();
    }

    public static Order toEntity(OrderDto dto) {
        return Order.builder()
                .no(dto.getNo())
                .user(UserDto.toEntity(dto.getUser()))
                .state(dto.getState())
                .date(dto.getDate())
                .price(dto.getPrice())
                .orderProducts(dto.getOrderProducts().stream()
                        .map(OrderProductDto::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
