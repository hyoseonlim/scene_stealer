package pack.entity;


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
import pack.dto.ReportedPostDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reported_posts")
public class ReportedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "post_no")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    private String category;
    public static ReportedPostDto toDto(ReportedPost entity) {
    	return ReportedPostDto.builder()
    			.no(entity.getNo())
    			.postNo(entity.getPost().getNo())
    			.postWriteUserNo(entity.getPost().getUser().getNo())
    			.userNo(entity.getUser().getNo())
//    			.post(Post.toDto(entity.getPost()))
//    			.user(User.toDto(entity.getUser()))
    			.category(entity.getCategory())
    			.build();
    }
}
