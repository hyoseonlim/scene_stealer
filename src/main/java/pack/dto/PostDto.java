package pack.dto;

import java.util.Date;  // java.sql.Date 대신 java.util.Date 사용
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Post;
import pack.entity.Product;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

	private Integer no;

	private UserDto user;

	private String content;

	private Date date;

	private String pic; // URL or file path
	
	private MultipartFile image; // 이미지 파일을 저장할 필드

	private Integer likesCount;
	private Integer commentsCount;
	private Integer reportsCount;

    // 소프트 삭제 관련 필드
	private boolean deleted;
	private Date deletedAt;  // java.util.Date로 수정

	private ProductDto product;
	private List<CommentDto> comments = new ArrayList<>();
	private List<PostLikeDto> postLikes = new ArrayList<>();
	private List<ReportedPostDto> reportedPosts = new ArrayList<>();
	
	private Integer productNo, userNo;
	private String userNickname, userPic, userId;
	private List<Integer> commentsList;
	private List<Integer> reportedPostsList;
	
	private int totalPages, currentPage;
	private Long totalElements;

    // DTO에서 Entity로 변환하는 메서드
	public static Post toEntity(PostDto dto) {
		return Post.builder()
				.no(dto.getNo())
				.user(User.builder().no(dto.getUserNo()).build())
				.content(dto.getContent())
				.date(dto.getDate())
				.pic(dto.getPic())
				.likesCount(dto.getLikesCount())
				.commentsCount(dto.getCommentsCount())
				.reportsCount(dto.getReportsCount())
				.product(Product.builder().no(dto.getProductNo()).build())
				.deleted(dto.isDeleted())  // 소프트 삭제 상태 추가
				.deletedAt(dto.getDeletedAt())  // 삭제된 시점 추가
				.build();
	}
}
