package pack.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Post;
import pack.entity.ReportedPost;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReportedPostsDto {
	 private Integer no;
	 private Post post;
	 private User user;
	 private String category;
	 public static ReportedPost toEntity(ReportedPostsDto dto) {
		 return ReportedPost.builder()
				 .no(dto.getNo())
	    			.post(dto.getPost())
	    			.user(dto.getUser())
	    			.category(dto.getCategory())
	    			.build();
	 }
}
