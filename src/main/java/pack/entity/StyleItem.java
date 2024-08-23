package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.StyleItemDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Style_Item")
public class StyleItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	@ManyToOne
	@JoinColumn(name = "style_no")
	private Style style;
	
	@ManyToOne
	@JoinColumn(name = "item_no")
	private Item item;
	
	public static StyleItemDto toDto(StyleItem entity) {
		return StyleItemDto.builder()
				.no(entity.getNo())
				.styleNo(entity.getStyle().getNo())
				.itemNo(entity.getItem().getNo())
				.build();
	}

}
