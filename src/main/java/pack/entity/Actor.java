package pack.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.ActorDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    private String name;

    private String pic;

    @OneToMany(mappedBy = "actor")
    @Builder.Default
    private List<Character> characters = new ArrayList<>();

    public static ActorDto toDto(Actor entity) {
        return ActorDto.builder()
                .no(entity.getNo())
                .name(entity.getName())
                .pic(entity.getPic())
                .characters(entity.getCharacters().stream().map(Character::toDto).collect(Collectors.toList()))
                .build();
    }
}
