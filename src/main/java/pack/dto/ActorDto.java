package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Actor;

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
    private List<Integer> showActorNo;
    private List<String> showDetails;
    
    public ActorDto(String name, Integer no, List<String> showDetails) {
        this.name = name;
        this.no = no;
        this.showDetails = showDetails;
    }
    
    public static Actor toEntity(ActorDto dto) {
        return Actor.builder()
               .no(dto.getNo())
               .name(dto.getName())
               .pic(dto.getPic())
               // null인 상태에서 stream()을 호출 시 발생하는 에러 방지
               //.characters(dto.getCharacters() == null ? new ArrayList<>() : dto.getCharacters().stream().map(CharacterDto::toEntity).collect(Collectors.toList()))
               .build();
    }
}
