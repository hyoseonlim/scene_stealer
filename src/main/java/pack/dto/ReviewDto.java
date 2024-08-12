package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Product;
import pack.entity.Review;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
	 private Integer no;
	 
	 private User user;
	 private Product product;

	 private String contents;
	    
	 private String pic;  // URL or file path

	 private Integer score;
	 
	 public static Review toEntity(ReviewDto dto) {
			return Review.builder()
					.no(dto.getNo())
	    			.user(dto.getUser())
	    			.product(dto.getProduct())
	    			.contents(dto.getContents())
	    			.pic(dto.getPic())
	    			.score(dto.getScore())
	    			.build();
		}

}
