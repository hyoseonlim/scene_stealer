package pack.entity;

import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_comment_no")
    private Comment parentComment;
    
    private String content;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "date")
    private java.util.Date date;
     
    @OneToMany(mappedBy = "comment")
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();
    
    public static CommentDto toDto(Comment entity) {
    	CommentDto.CommentDtoBuilder dtoBuilder = CommentDto.builder()
    	        .no(entity.getNo())
    	        .postNo(entity.getPost().getNo())
    	        .userNo(entity.getUser().getNo())
    	        .userNickname(entity.getUser().getNickname())
    	        .content(entity.getContent())
    	        .date(entity.getDate());

    	    if (entity.getParentComment() != null) {
    	        dtoBuilder.parentCommentNo(entity.getParentComment().getNo());
    	        if (entity.getParentComment().getUser() != null) {
    	            dtoBuilder.userNickname(entity.getParentComment().getUser().getNickname());
    	        }
    	    }

    	    return dtoBuilder.build();
    }
}
