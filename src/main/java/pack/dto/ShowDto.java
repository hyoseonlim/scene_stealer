package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Show;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {
    private Integer no;
    private String title;
    private String pic;
    private List<ShowActorDto> showActors;

    public Show toEntity() {
        return Show.builder()
                .no(this.no)
                .title(this.title)
                .pic(this.pic)
                .showActor(this.showActors != null ? 
                		this.showActors.stream()
                                .map(ShowActorDto::toEntity)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}
