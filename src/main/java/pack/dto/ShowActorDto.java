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
    
    private Integer showNo, actorNo;

    public static ShowActor toEntity(ShowActorDto dto) {
        return ShowActor.builder()
                .no(dto.getNo())
                .show(ShowDto.toEntity(dto.getShow()))
                .actor(ActorDto.toEntity(dto.getActor()))
                .build();
    }
}
