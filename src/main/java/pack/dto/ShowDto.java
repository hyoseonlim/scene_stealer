package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {
    private Integer no;
    private String title;
    private String pic;
    private List<ShowActorDto> showActors;
    private List<CharacterDto> characters;

    private List<Integer> showActorsNo;
    private List<Integer> characterNo;
    private List<String> actorNames; // 배우 이름 필드 추가

    public static Show toEntity(ShowDto dto) {
        return Show.builder()
                .no(dto.getNo())
                .title(dto.getTitle())
                .pic(dto.getPic())
                .showActor(dto.getShowActors() != null 
                           ? dto.getShowActors().stream().map(ShowActorDto::toEntity).collect(Collectors.toList()) 
                           : new ArrayList<>())
                .characters(dto.getCharacters() != null 
                            ? dto.getCharacters().stream().map(CharacterDto::toEntity).collect(Collectors.toList()) 
                            : new ArrayList<>())
                .build();
    }
}