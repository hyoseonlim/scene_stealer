package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterLikeDto {
	private Integer no;
	private CharacterDto character;
	private UserDto user;
	
	private Integer characterNo, userNo;
	
	private int totalPages, currentPage;
	private Long totalElements;
	
    public static CharacterLike toEntity(CharacterLikeDto dto) {
    	return CharacterLike.builder()
    			.no(dto.getNo())
    			.character(Character.builder().no(dto.getCharacterNo()).build())
                .user(User.builder().no(dto.getUserNo()).build())
    			.build();
    }
	

}
