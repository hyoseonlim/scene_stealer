package pack.entity;
import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.OrderDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne()
    @JoinColumn(name = "user_no")
    private User user;

    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "date")
    private java.util.Date date;

    private Integer price;

    @OneToMany(mappedBy = "order")
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();

		public static OrderDto toDto (Order entity) {
	    	return OrderDto.builder()
	    			.no(entity.getNo())
	    			.userNo(entity.getUser().getNo())
	    			.userName(entity.getUser().getName())
//	    			.user(User.toDto(entity.getUser()))
	    			.state(entity.getState())
	    			.date(entity.getDate())
	    			.price(entity.getPrice())
	    			.productNoList(entity.getOrderProducts().stream().map((x) -> x.getProduct().getNo()).collect(Collectors.toList()))
	    			.orderProducts(entity.getOrderProducts().stream().map(OrderProduct::toDto).collect(Collectors.toList()))
	    			.build();
	    }
}
