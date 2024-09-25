package pack.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.ActorDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actors") // 데이터베이스의 actors 테이블과 매핑
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키를 자동으로 생성
    private Integer no; // 배우의 고유 ID

    private String name; // 배우의 이름

    private String pic; // 배우의 사진

    @OneToMany(mappedBy = "actor") // Actor가 여러 Character를 가질 수 있음을 나타냄
    @Builder.Default
    private List<Character> characters = new ArrayList<>(); // 해당 배우가 관련된 Character 목록

    @OneToMany(mappedBy = "actor") // Actor가 여러 ShowActor를 가질 수 있음을 나타냄
    private Set<ShowActor> showActors; // 해당 배우가 관련된 ShowActor 목록

    // 엔티티를 DTO로 변환하는 메소드
    public static ActorDto toDto(Actor entity) {
        return ActorDto.builder()
                .no(entity.getNo()) // 배우의 ID
                .name(entity.getName()) // 배우의 이름
                .pic(entity.getPic()) // 배우의 사진 URL
                .showActorNo(entity.getShowActors().stream().map(ShowActor::getNo).toList()) // 관련된 ShowActor의 ID 목록
                .characterNo(entity.getCharacters().stream().map(Character::getNo).toList()) // 관련된 Character의 ID 목록
//                .characters(entity.getCharacters().stream().map(Character::toDto).collect(Collectors.toList())) // Character 객체를 DTO로 변환 (주석 처리됨)
                .build(); // DTO 객체를 생성
    }
}