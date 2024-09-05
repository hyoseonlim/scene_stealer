package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
	    
	    @OneToOne(mappedBy = "orderProduct")
	    private Review review;

	    private Integer price;
	    private Integer quantity;
	    
	    public static OrderProductDto toDto(OrderProduct entity) {
	        // 방어 코드 추가: product와 order가 null일 수 있는 가능성 고려
	        return OrderProductDto.builder()
	                .no(entity.getNo())
	                .orderNo(entity.getOrder() != null ? entity.getOrder().getNo() : null)
	                .productNo(entity.getProduct() != null ? entity.getProduct().getNo() : null)
	                .price(entity.getPrice())
	                .reviewNo(entity.getReview() == null ? null : entity.getReview().getNo())
//	                .review(Review.toDto(entity.getReview()))
	                .quantity(entity.getQuantity())
	                .build();
	    }

}
