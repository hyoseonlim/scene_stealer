package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.ShowActor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowActorDto {
    private Integer no;
    private ShowDto show;
    private ActorDto actor;

    public ShowActor toEntity() {
        return ShowActor.builder()
                .no(this.no)
                .show(this.show != null ? this.show.toEntity() : null)
                .actor(this.actor != null ? this.actor.toEntity() : null)
                .build();
    }
}
