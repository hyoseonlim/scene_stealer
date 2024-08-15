package pack.entity;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.ShowDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @OneToMany(mappedBy = "show")
    @Builder.Default
    private List<Character> characters = new ArrayList<>();

    private String title;

    private String pic;

    @OneToMany(mappedBy = "show")
    @Builder.Default
    private List<ShowActor> showActor = new ArrayList<>();
    
    public static ShowDto toDto(Show entity) {
        return ShowDto.builder()
                .no(entity.getNo())
                .title(entity.getTitle())
                .pic(entity.getPic())
                .showActors(entity.getShowActor().stream().map(ShowActor::toDto).collect(Collectors.toList()))
                .characters(entity.getCharacters().stream().map(Character::toDto).collect(Collectors.toList()))
                .build();
    }
}

