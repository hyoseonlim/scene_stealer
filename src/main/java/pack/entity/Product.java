package pack.entity;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.ProductDto;

@Getter
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
    private Integer discountRate;
    private java.math.BigDecimal score;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();
    
    public static Product toEntity(ProductDto dto) {
    	return Product.builder()
    			.no(dto.getNo())
    			.name(dto.getName())
    			.price(dto.getPrice())
    			.contents(dto.getContents())
    			.date(dto.getDate())
    			.category(dto.getCategory())
    			.pic(dto.getPic())
    			.stock(dto.getStock())
    			.discountRate(dto.getDiscountRate())
    			.score(dto.getScore())
    			.build();
    }
}

