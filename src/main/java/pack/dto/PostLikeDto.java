package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Post;
import pack.entity.PostLike;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeDto {
	private Integer no;
	private PostDto post;
	private UserDto user;
	private Integer postNo, userNo;

	public static PostLike toEntity(PostLikeDto dto) {
		return PostLike.builder()
				.no(dto.getNo())
				.post(Post.builder().no(dto.getPostNo()).build())
				.user(User.builder().no(dto.getUserNo()).build())
				.build();
	}
}
