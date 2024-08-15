package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Character;
import pack.entity.Show;
import pack.entity.ShowActor;

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

	public static Show toEntity(ShowDto dto) {
        return Show.builder()
                .no(dto.getNo())
                .title(dto.getTitle())
                .pic(dto.getPic())
                .showActor(dto.getShowActors().stream().map(ShowActorDto::toEntity).collect(Collectors.toList()))
                .characters(dto.getCharacters().stream().map(CharacterDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
