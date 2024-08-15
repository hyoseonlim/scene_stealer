package pack.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Character;
import pack.entity.Actor;
import pack.entity.Show;
import pack.entity.Style;
import pack.entity.CharacterLike; // Ensure CharacterLike is imported

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
                .likesCount(dto.getLikesCount())
                .pic(dto.getPic())
                .actor(ActorDto.toEntity(dto.getActor()))
                .show(ShowDto.toEntity(dto.getShow())) 
                .characterLikes(dto.getCharacterLikes().stream().map(CharacterLikeDto::toEntity).collect(Collectors.toList()))
                .styles(dto.getStyles().stream().map(StyleDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
