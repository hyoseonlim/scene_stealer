package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Actor;
import pack.entity.Show;
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
    
    private Integer showNo, actorNo;

    public static ShowActor toEntity(ShowActorDto dto) {
        return ShowActor.builder()
                .no(dto.getNo())
                .show(Show.builder().no(dto.getShowNo()).build())
                .actor(Actor.builder().no(dto.getActorNo()).build())
                .build();
    }
}
