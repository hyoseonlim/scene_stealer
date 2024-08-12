package pack.entity;

import java.util.List;
import java.util.stream.Collectors;

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

    private String title;

    private String pic;

    @OneToMany(mappedBy = "show")
    @Builder.Default
    private List<ShowActor> showActor = new ArrayList<>();
    
    public ShowDto toDto() {
        return ShowDto.builder()
                .no(this.no)
                .title(this.title)
                .pic(this.pic)
                .showActors(this.showActor.stream()
                        .map(ShowActor::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}

