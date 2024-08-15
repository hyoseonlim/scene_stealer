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
public class ReportedPostDto {
	 private Integer no;
	 private PostDto post;
	 private UserDto user;
	 private String category;
	 public static ReportedPost toEntity(ReportedPostDto dto) {
		 return ReportedPost.builder()
				 .no(dto.getNo())
	    			.post(PostDto.toEntity(dto.getPost()))
	    			.user(UserDto.toEntity(dto.getUser()))
	    			.category(dto.getCategory())
	    			.build();
	 }
}
