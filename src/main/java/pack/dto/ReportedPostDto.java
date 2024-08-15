package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.ReportedPost;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportedPostDto {
	 private Integer no;
	 private PostDto post;
	 private UserDto user;
	 private String category;
	 
	 private Integer postNo, postWriteUserNo, userNo; 
	 
	 public static ReportedPost toEntity(ReportedPostDto dto) {
		 return ReportedPost.builder()
				 .no(dto.getNo())
	    			.post(PostDto.toEntity(dto.getPost()))
	    			.user(UserDto.toEntity(dto.getUser()))
	    			.category(dto.getCategory())
	    			.build();
	 }
}
