package pack.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Comment;
import pack.entity.Post;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private Integer no;
	private PostDto post;
	private UserDto user;
//	private CommentDto parentComment;
	private java.util.Date date;
	private String content;
	private Integer postNo, userNo, parentCommentNo;

	public static Comment toEntity(CommentDto dto) {
		return Comment.builder()
				.no(dto.getNo())
				.post(PostDto.toEntity(dto.getPost()))
				.user(UserDto.toEntity(dto.getUser()))
				.parentCommentNo(dto.getParentCommentNo())
				.content(dto.getContent())
				.date(dto.getDate())
				.build();
	}
}
