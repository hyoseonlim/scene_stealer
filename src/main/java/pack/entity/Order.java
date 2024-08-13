package pack.entity;
import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.OrderDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    private String state;

    @Column(name = "date")
    private java.util.Date date;

    private Integer price;

    @OneToMany(mappedBy = "order")
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();


		public static OrderDto toDto (Order entity) {
	    	return OrderDto.builder()
	    			.no(entity.getNo())
	    			.user(entity.getUser())
	    			.state(entity.getState())
	    			.date(entity.getDate())
	    			.price(entity.getPrice())
	    			.orderProducts(entity.getOrderProducts())
	    			.build();
	    }
}
