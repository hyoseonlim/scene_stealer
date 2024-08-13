package pack.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Post;
import pack.entity.PostLike;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeDto {
	 private Integer no;
	 private Post post;
	  private User user;
	  public static PostLike toEntity(PostLikeDto dto) {
		  return PostLike.builder()
				  .no(dto.getNo())
				  .post(dto.getPost())
				  .user(dto.getUser())
				  .build();
	  }
}
