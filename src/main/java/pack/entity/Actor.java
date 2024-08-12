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

    public ActorDto toDto() {
        return ActorDto.builder()
                .no(this.no)
                .name(this.name)
                .pic(this.pic)
                .characters(this.characters != null ? 
                    this.characters.stream()
                                    .map(Character::toDto) // Convert each Character to CharacterDto
                                    .collect(Collectors.toList()) : null)
                .build();
    }
}
