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
import pack.dto.ReportedPostsDto;

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
    public static ReportedPostsDto toDto(ReportedPost entity) {
    	return ReportedPostsDto.builder()
    			.no(entity.getNo())
    			.post(entity.getPost())
    			.user(entity.getUser())
    			.category(entity.getCategory())
    			.build();
    }
}
