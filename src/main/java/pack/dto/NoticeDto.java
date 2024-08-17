package pack.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Notice;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
	 private Integer no;

	 private String category;
	 private String title;
	 private String contents;
	 
	 private LocalDateTime date;
	 
	 public static Notice toEntity(NoticeDto dto) {
			return Notice.builder()
					.no(dto.getNo())
	    			.category(dto.getCategory())
	    			.title(dto.getTitle())
	    			.contents(dto.getContents())
	    			.date(dto.getDate())
	    			.build();
		}
}
