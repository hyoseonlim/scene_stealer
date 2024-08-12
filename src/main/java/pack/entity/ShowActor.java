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
    private Show show;

    @ManyToOne
    @JoinColumn(name = "actor_no")
    private Actor actor;

    public ShowActorDto toDto() {
        return ShowActorDto.builder()
                .no(this.no)
                .show(this.show != null ? this.show.toDto() : null)
                .actor(this.actor != null ? this.actor.toDto() : null)
                .build();
    }
}
