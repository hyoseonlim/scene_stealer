package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.PostLike;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeDto {
	private Integer no;
	private PostDto post;
	private UserDto user;

	public static PostLike toEntity(PostLikeDto dto) {
		return PostLike.builder().no(dto.getNo()).post(PostDto.toEntity(dto.getPost())).user(UserDto.toEntity(dto.getUser())).build();
	}
}
