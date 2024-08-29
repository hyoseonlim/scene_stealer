package pack.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.PostDto;

@Getter
@Setter
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

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "date")
    private Date date;

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

    // 소프트 삭제 관련 필드 추가
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    // 엔티티를 DTO로 변환하는 메서드
    public static PostDto toDto(Post entity) {
        return PostDto.builder()
            .no(entity.getNo())
            .content(entity.getContent())
            .date(entity.getDate())
            .pic(entity.getPic())
            .userId(entity.getUser().getId())
            .likesCount(entity.getLikesCount())
            .commentsCount(entity.getCommentsCount())
            .reportsCount(entity.getReportsCount())
            .reportedPostsList(entity.getReportedPosts().stream().map(ReportedPost::getNo).collect(Collectors.toList()))
            .productNo(entity.getProduct() != null ? entity.getProduct().getNo() : null)
            .userNickname(entity.getUser() != null ? entity.getUser().getNickname() : null)
            .userNo(entity.getUser() != null ? entity.getUser().getNo() : null)
            .userPic(entity.getUser() != null ? entity.getUser().getPic() : null)
            .commentsList(entity.getComments().stream().map(Comment::getNo).collect(Collectors.toList()))
            .deleted(entity.isDeleted())  // 소프트 삭제 상태 추가
            .deletedAt(entity.getDeletedAt())  // 삭제된 시점 추가
            .build();
    }
}
