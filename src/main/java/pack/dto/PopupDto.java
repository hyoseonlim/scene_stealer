package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Popup;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupDto {

	private Integer no;

	private String pic;
	private String path;
	
	private Boolean isShow;
	
	public static Popup toEntity (PopupDto dto) {
		return Popup.builder()
				.no(dto.getNo())
				.pic(dto.getPic())
				.path(dto.getPath())
				.isShow(dto.getIsShow())
				.build();
	}
}
