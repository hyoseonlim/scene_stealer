package pack.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Actor;
import pack.entity.Character; // Ensure Character is imported

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {
    private Integer no;
    private String name;
    private String pic;
    private List<CharacterDto> characters;
    private List<Integer> characterNo;

    public static Actor toEntity(ActorDto dto) {
        return Actor.builder()
               .no(dto.getNo())
               .name(dto.getName())
               .pic(dto.getPic())
               .characters(dto.getCharacters().stream().map(CharacterDto::toEntity).collect(Collectors.toList()))
               .build();
    }
}
