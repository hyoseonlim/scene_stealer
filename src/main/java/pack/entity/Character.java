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
    
    @OneToMany(mappedBy = "character")
    @Builder.Default
    private List<Style> styles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "show_no")
    private Show show;

    @OneToMany(mappedBy = "character")
    @Builder.Default
    private List<CharacterLike> characterLikes = new ArrayList<>();

    public static CharacterDto toDto(Character entity) {
        return CharacterDto.builder()
                .no(entity.getNo())
                .name(entity.getName())
                .likesCount(entity.getLikesCount())
                .pic(entity.getPic())
                .actorNo(entity.getActor().getNo())
                .showNo(entity.getShow().getNo())
                .characterLikeNo(entity.getCharacterLikes().stream().map(characterLike -> characterLike.getUser().getNo()).collect(Collectors.toList()))
                .styleNo(entity.getStyles().stream().map(Style::getNo).collect(Collectors.toList()))
//                .actor(Actor.toDto(entity.getActor()))
//                .show(Show.toDto(entity.getShow()))
//                .characterLikes(entity.getCharacterLikes().stream().map(CharacterLike::toDto).collect(Collectors.toList()))
//                .styles(entity.getStyles().stream().map(Style::toDto).collect(Collectors.toList()))
                .build();
    }
}
