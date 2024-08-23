package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.entity.Character;
import pack.entity.Follow;
import pack.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
    private Integer no;
    private UserDto followee;
    private UserDto follower;
    
    private List<Integer> followees;
    private List<Integer> followers;
    
    private Integer followeeNo, followerNo;

    private int totalPages, currentPage;
    private Long totalElements;

   public static Follow toEntity(FollowDto dto) {
	   return Follow.builder()
			   .no(dto.getNo())
			   .followee(User.builder().no(dto.getFolloweeNo()).build())
			   .follower(User.builder().no(dto.getFollowerNo()).build())
			   .build();
   }
    
}
