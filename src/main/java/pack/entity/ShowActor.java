package pack.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private Show show;

    @ManyToOne
    @JoinColumn(name = "actor_no")
    private Actor actor;

    public static ShowActorDto toDto(ShowActor entity) {
        return ShowActorDto.builder()
                .no(entity.getNo())
                .show(Show.toDto(entity.getShow()))
                .actor(Actor.toDto(entity.getActor()))
                .build();
    }
}
