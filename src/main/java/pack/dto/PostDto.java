package pack.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import pack.entity.Comment;
import pack.entity.Post;
import pack.entity.PostLike;
import pack.entity.Product;
import pack.entity.ReportedPost;
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

	public static Post toEntity(PostDto dto) {
		return Post.builder().no(dto.getNo()).user(UserDto.toEntity(dto.getUser())).content(dto.getContent())
				.date(dto.getDate()).pic(dto.getPic()).likesCount(dto.getLikesCount())
				.commentsCount(dto.getCommentsCount()).reportsCount(dto.getReportsCount())
				.product(ProductDto.toEntity(dto.getProduct())).build();
	}
}
