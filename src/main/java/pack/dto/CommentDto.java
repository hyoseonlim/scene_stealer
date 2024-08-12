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
	 private Post post;
	    private User user;
	    private Comment parentComment;
	    private java.util.Date date;
	    private String content;
	    public static Comment toEntity(CommentDto dto) {
	    	return Comment.builder()
	    			.no(dto.getNo())
	    			.post(dto.getPost())
	    			.user(dto.getUser())
	    			.parentComment(dto.getParentComment())
	    			.content(dto.getContent())
	    			.date(dto.getDate())
	    			.build();
	    }
}
