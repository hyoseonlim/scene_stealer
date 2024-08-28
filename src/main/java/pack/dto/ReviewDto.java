package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Review;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
	 private Integer no;
	 
	 private UserDto user;

	 private ProductDto product;
	 
	 private String contents;
	    
	 private String pic;  // URL or file path

	 private Integer score;
	 
	 private Integer userNo, productNo;
	 private String userNickname,userid, productName;
	 
	 private int totalPages, currentPage;
	 private Long totalElements;
	 
	 public static Review toEntity(ReviewDto dto) {
			return Review.builder()
					.no(dto.getNo())
	    			.user(UserDto.toEntity(dto.getUser()))
	    			.product(ProductDto.toEntity(dto.getProduct()))
	    			.contents(dto.getContents())
	    			.pic(dto.getPic())
	    			.score(dto.getScore())
	    			.build();
		}

}
