package pack.entity;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.CommentLikeDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_like")
public class CommentLike {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer no;

	    @ManyToOne
	    @JoinColumn(name = "comment_no")
	    private Comment comment;

	    @ManyToOne
	    @JoinColumn(name = "user_no")
	    private User user;
	    
	    public static CommentLikeDto toDto(CommentLike entity) {
	    	return CommentLikeDto.builder()
	    			.no(entity.getNo())
	    			.commentNo(entity.getComment().getNo())
	    			.userNo(entity.getUser().getNo())
//	    			.comment(Comment.toDto(entity.getComment()))
//	    			.user(User.toDto(entity.getUser()))
	    			.build();
	    }

}
