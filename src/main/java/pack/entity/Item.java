package pack.entity;

import jakarta.persistence.Column;
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
import pack.dto.ItemDto;

@Setter // 관리자 아이템 추가 시 에러 해결을 위해 불가피한 사용
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	@Column(name = "pic")
	private String pic; // URL or file path
	
	private String name;
	
	private String path;

	@ManyToOne
	@JoinColumn(name = "product_no")
	private Product product;

	public static ItemDto toDto(Item entity) {
		return ItemDto.builder()
				.no(entity.getNo())
				.pic(entity.getPic())
				.name(entity.getName())
				.productNo(entity.getProduct().getNo())
				.path(entity.getPath())
				.build();
	}
}
