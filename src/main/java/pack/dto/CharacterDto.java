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
    private ActorDto actor; // Assuming ActorDto is the DTO for Actor
    private ShowDto show;   // Assuming ShowDto is the DTO for Show
    private List<CharacterLikeDto> characterLikes; // Assuming CharacterLikeDto is the DTO for CharacterLike

    // Convert CharacterDto to Character entity
    public Character toEntity() {
        return Character.builder()
                .no(this.no)
                .name(this.name)
                .likesCount(this.likesCount)
                .pic(this.pic)
                .actor(this.actor != null ? this.actor.toEntity() : null) // Ensure actor is not null before calling toEntity
                .show(this.show != null ? this.show.toEntity() : null) // Ensure show is not null before calling toEntity
                .characterLikes(this.characterLikes != null ?
                    this.characterLikes.stream()
                                        .map(CharacterLikeDto::toEntity) // Convert each CharacterLikeDto to CharacterLike
                                        .collect(Collectors.toList()) : null)
                .build();
    }
}
