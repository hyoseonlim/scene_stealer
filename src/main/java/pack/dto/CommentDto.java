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
	private CommentDto parentComment;
	private java.util.Date date;
	private String content, userNickname;
	private Integer postNo, userNo, parentCommentNo;

	public static Comment toEntity(CommentDto dto) {
		
	    Comment parentCommentEntity = null;
	    
	    if (dto.getParentCommentNo() != null) {
	        parentCommentEntity = Comment.builder().no(dto.getParentCommentNo()).build();
	    }

	    return Comment.builder()
	            .no(dto.getNo())
	            .post(Post.builder().no(dto.getPostNo()).build())
	            .user(User.builder().no(dto.getUserNo()).build())
	            .parentComment(parentCommentEntity)
	            .content(dto.getContent())
	            .date(dto.getDate())
	            .build();
	}
}
