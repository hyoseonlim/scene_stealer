package pack.entity;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.ShowActorDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "show_actor")
public class ShowActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "show_no")
    private Show show;		// 작품을 찾기 위해

    @ManyToOne
    @JoinColumn(name = "actor_no")
    private Actor actor;	// 배우를 찾기 위해

    public static ShowActorDto toDto(ShowActor entity) {
        return ShowActorDto.builder()
                .no(entity.getNo())
                .showNo(entity.getShow().getNo())
                .actorNo(entity.getActor().getNo())
//                .show(Show.toDto(entity.getShow()))
//                .actor(Actor.toDto(entity.getActor()))
                .build();
    }
}
