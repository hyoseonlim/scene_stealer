package pack.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Product;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer no;
    
    private String name;
    private Integer price;
    private String contents;

    private Date date;
    private Integer reviewCount;

    private String category;
    private PostDto post;

    private String pic; // URL or file path

    private Integer stock;
    private Integer count;
    private Integer discountRate;
    private java.math.BigDecimal score;
    
    private List<Integer> reviewNoList, orderProductNoList;

    private List<OrderProductDto> orderProducts = new ArrayList<>();
    
    // 엔티티로 변환 메서드
    public static Product toEntity(ProductDto dto) {
        return Product.builder()
                .no(dto.getNo())
                .name(dto.getName())
                .price(dto.getPrice())
                .contents(dto.getContents())
                // 날짜가 설정되지 않은 경우 현재 날짜로 설정
                .date(dto.getDate() != null ? dto.getDate() : new Date())
                .category(dto.getCategory())
                .pic(dto.getPic())
                .stock(dto.getStock())
                .count(dto.getCount())
                .discountRate(dto.getDiscountRate())
                .score(dto.getScore())
                .orderProducts(dto.getOrderProducts().stream().map(OrderProductDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
