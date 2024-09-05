package pack.entity;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.OrderProductDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    private String name;
    private Integer price;
    private String contents;

    @Column(name = "date")
    private java.util.Date date;

    private String category;

    @Column(name = "pic")
    private String pic;  // URL or file path

    private Integer stock;
    private Integer count;
    private Integer discountRate;
    private java.math.BigDecimal score;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();
    
    @OneToMany(mappedBy = "product" )
    private List<Post> post;
    
    public static ProductDto toDto(Product entity) {
    	return ProductDto.builder()
    			.no(entity.getNo())
    			.name(entity.getName())
    			.price(entity.getPrice())
    			.contents(entity.getContents())
    			.date(entity.getDate())
    			.category(entity.getCategory())
    			.pic(entity.getPic())
    			.stock(entity.getStock())
    			.count(entity.getCount())
    			.discountRate(entity.getDiscountRate())
    			.score(entity.getScore())
    			.orderProductNoList(entity.getOrderProducts().stream().map(OrderProduct::getNo).collect(Collectors.toList()))
//    			.reviews(entity.getReviews().stream().map(Review::toDto).collect(Collectors.toList()))
//    			.orderProducts(entity.getOrderProducts().stream().map(OrderProduct::toDto).collect(Collectors.toList()))
    			.build();
    }
}
