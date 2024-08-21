package pack.entity;

import java.util.*;
import java.util.stream.Collectors;

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
        List<String> actorNames = entity.getShowActor().stream()
                                        .map(s -> s.getActor().getName())
                                        .collect(Collectors.toList());
        List<String> actorPics = entity.getShowActor().stream()
                                       .map(s -> s.getActor().getPic())
                                       .collect(Collectors.toList());

        // 디버깅 로그 추가
        System.out.println("Actor Names: " + actorNames);
        System.out.println("Actor Pics: " + actorPics);

        return ShowDto.builder()
                .no(entity.getNo())
                .title(entity.getTitle())
                .pic(entity.getPic())
                .characterNo(entity.getCharacters().stream().map(Character::getNo).collect(Collectors.toList()))
                .showActorsNo(entity.getShowActor().stream().map(s -> s.getActor().getNo()).collect(Collectors.toList()))
                .actorNames(actorNames)
                .actorPics(actorPics) // actorPics 추가
                .build();
    }
}