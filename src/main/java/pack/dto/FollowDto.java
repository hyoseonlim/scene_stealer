package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Follow;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
    private Integer no;
    private User followee;
    private User follower;

   public static Follow toEntity(FollowDto dto) {
	   return Follow.builder()
			   .no(dto.getNo())
			   .followee(dto.getFollowee())
			   .follower(dto.getFollower())
			   .build();
   }
    
}
