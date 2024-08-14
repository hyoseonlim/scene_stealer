package pack.entity;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.PostDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    private String content;

    @Column(name = "date")
    private java.util.Date date;

    @Column(name = "pic")
    private String pic;  // URL or file path

    private Integer likesCount;
    private Integer commentsCount;
    private Integer reportsCount;

    @ManyToOne
    @JoinColumn(name = "product_no")
    private Product product;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<ReportedPost> reportedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<PostLike> postLikes = new ArrayList<>();
    public static PostDto toDto(Post entity) {
    	return PostDto.builder()
    			.no(entity.getNo())
    			.user(entity.getUser())
    			.content(entity.getContent())
    			.date(entity.getDate())
    			.pic(entity.getPic())
    			.likesCount(entity.getLikesCount())
    			.commentsCount(entity.getCommentsCount())
    			.reportsCount(entity.getReportsCount())
    			.product(entity.getProduct())
    			.build();
    }
}
