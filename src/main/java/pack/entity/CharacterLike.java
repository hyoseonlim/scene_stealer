package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.CharacterLikeDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "character_like")
public class CharacterLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "character_no")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;
    
	public static CharacterLikeDto toDto (CharacterLike entity) {
		return CharacterLikeDto.builder()
				.no(entity.getNo())
				.character(Character.toDto(entity.getCharacter()))
				.user(User.toDto(entity.getUser()))
				.build();
	}
}
