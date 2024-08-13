package pack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.AdminDto;
import pack.dto.ReviewDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_no")
    private Product product;

    private String contents;

    @Column(name = "pic")
    private String pic;  // URL or file path

    private Integer score;
    
    public static ReviewDto toDto (Review entity) {
    	return ReviewDto.builder()
    			.no(entity.getNo())
    			.user(entity.getUser())
    			.product(entity.getProduct())
    			.contents(entity.getContents())
    			.pic(entity.getPic())
    			.score(entity.getScore())
    			.build();
    }
}
