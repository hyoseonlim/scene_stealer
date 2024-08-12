package pack.dto;

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

    public Character toEntity() {
        return Character.builder()
                .no(this.no)
                .name(this.name)
                .likesCount(this.likesCount)
                .pic(this.pic)
                .actor(this.actor != null ? this.actor.toEntity() : null)
                .show(this.show != null ? this.show.toEntity() : null)
                .characterLikes(this.characterLikes != null ?
                    this.characterLikes.stream()
                                        .map(CharacterLikeDto::toEntity)
                                        .collect(Collectors.toList()) : null)
                .build();
    }
}
