package pack.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.CommentDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "post_no")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

//    @ManyToOne
//    @JoinColumn(name = "parent_comment_no")
//    private Comment parentComment;
    
    private Integer parentCommentNo;

    private String content;

    @Column(name = "date")
    private java.util.Date date;
    
    public static CommentDto toDto(Comment entity) {
    	return CommentDto.builder()
    			.no(entity.getNo())
    			.postNo(entity.getPost().getNo())
    			.userNo(entity.getUser().getNo())
    			.parentCommentNo(entity.getParentCommentNo())
//    			.post(Post.toDto(entity.getPost()))
//    			.user(User.toDto(entity.getUser()))
//    			.parentComment(Comment.toDto(entity.getParentComment()))
    			.content(entity.getContent())
    			.date(entity.getDate())
    			.build();
    }
}
