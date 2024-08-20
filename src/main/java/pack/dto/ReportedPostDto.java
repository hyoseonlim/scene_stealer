package pack.dto;

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
	 
	 private Integer postNo, postWriteUserNo, userNo; 
	 
	 public static ReportedPost toEntity(ReportedPostDto dto) {
		 return ReportedPost.builder()
				 .no(dto.getNo())
	    			.post(Post.builder().no(dto.getPostNo()).build())
	    			.user(User.builder().no(dto.getUserNo()).build())
	    			.category(dto.getCategory())
	    			.build();
	 }
}
