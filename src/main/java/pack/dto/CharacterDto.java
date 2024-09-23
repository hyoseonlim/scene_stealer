package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Actor;
import pack.entity.Character;
import pack.entity.Show;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDto {
    private Integer no;
    private String name;
    private Integer likesCount;
    private String pic;
    private ActorDto actor;
    private ShowDto show;
    private List<CharacterLikeDto> characterLikes;
    private List<StyleDto> styles;
    
    private Integer actorNo, showNo;
    private List<Integer> characterLikeNo;
    private List<Integer> styleNo;

    public static Character toEntity(CharacterDto dto) {
        return Character.builder()
                .no(dto.getNo())
                .name(dto.getName())
                .likesCount(dto.getLikesCount() != null ? dto.getLikesCount() : 0) // null 체크
                .pic(dto.getPic())
                .actor(Actor.builder().no(dto.getActorNo()).build())
                .show(Show.builder().no(dto.getShowNo()).build()) 
                //.characterLikes(dto.getCharacterLikes().stream().map(CharacterLikeDto::toEntity).collect(Collectors.toList()))
                //.styles(dto.getStyles().stream().map(StyleDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
