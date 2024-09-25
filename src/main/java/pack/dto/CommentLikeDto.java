package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Admin;
import pack.entity.Comment;
import pack.entity.CommentLike;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeDto {
	private Integer no;
	private CommentDto comment;
	private UserDto user;
	private Integer commentNo, userNo;

	public static CommentLike toEntity(CommentLikeDto dto) {
		return CommentLike.builder()
				.no(dto.getNo())
				.comment(Comment.builder().no(dto.getCommentNo()).build())
				.user(User.builder().no(dto.getUserNo()).build())
				.build();
	}

}
