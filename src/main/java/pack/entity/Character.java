package pack.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.CharacterDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    private String name;
    private Integer likesCount;

    @Column(name = "pic")
    private String pic;

    @ManyToOne
    @JoinColumn(name = "actor_no")
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "show_no")
    private Show show;

    @OneToMany(mappedBy = "character")
    @Builder.Default
    private List<CharacterLike> characterLikes = new ArrayList<>();

    public CharacterDto toDto() {
        return CharacterDto.builder()
                .no(this.no)
                .name(this.name)
                .likesCount(this.likesCount)
                .pic(this.pic)
                .actor(this.actor != null ? this.actor.toDto() : null)
                .show(this.show != null ? this.show.toDto() : null)
                .characterLikes(this.characterLikes != null ?
                    this.characterLikes.stream()
                                        .map(CharacterLike::toDto)
                                        .collect(Collectors.toList()) : null)
                .build();
    }
}
