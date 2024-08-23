package pack.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import pack.entity.Post;
import pack.entity.Product;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

	private Integer no;

	private UserDto user;

	private String content;

	private java.util.Date date;

	private String pic; // URL or file path

	private Integer likesCount;
	private Integer commentsCount;
	private Integer reportsCount;
	

	private ProductDto product;
	private List<CommentDto> comments = new ArrayList<>();
	private List<PostLikeDto> postLikes = new ArrayList<>();
	private List<ReportedPostDto> reportedPosts = new ArrayList<>();
	
	private Integer productNo, userNo;
	private String userNickname, userPic, userId;
	private List<Integer> commentsList;
	private List<Integer> reportedPostsList;
	
	private int totalPages, currentPage;
	private Long totalElements;

	public static Post toEntity(PostDto dto) {
		return Post.builder()
				.no(dto.getNo())
				.user(User.builder().no(dto.getUserNo()).build())
				.content(dto.getContent())
				.date(dto.getDate())
				.pic(dto.getPic())
				.likesCount(dto.getLikesCount())
				.commentsCount(dto.getCommentsCount())
				.reportsCount(dto.getReportsCount())
				.product(Product.builder().no(dto.getProductNo()).build())
				.build();
	}
}
