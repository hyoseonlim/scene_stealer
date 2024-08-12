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

    public Actor toEntity() {
        return Actor.builder()
                .no(this.no)
                .name(this.name)
                .pic(this.pic)
                .characters(this.characters != null ? 
                    this.characters.stream()
                                   .map(CharacterDto::toEntity)
                                   .collect(Collectors.toList()) : null)
                .build();
    }
}
