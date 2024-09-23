package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.PopupDto;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "popup")
public class Popup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private String pic;
	private String path;
	
	private Boolean isShow;
	
	public static PopupDto toDto (Popup entity) {
		return PopupDto.builder()
				.no(entity.getNo())
				.pic(entity.getPic())
				.path(entity.getPath())
				.isShow(entity.getIsShow())
				.build();
	}
	
}
