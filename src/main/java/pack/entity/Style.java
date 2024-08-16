package pack.entity;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pack.dto.CharacterLikeDto;
import pack.dto.StyleDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "styles")
public class Style {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	@Column(name = "pic")
	private String pic; // URL or file path

	@ManyToOne
	@JoinColumn(name = "character_no")
	private Character character;

	@OneToMany(mappedBy = "style")
	@Builder.Default
	private List<Item> item = new ArrayList<>();

	public static StyleDto toDto(Style entity) {
		return StyleDto.builder()
				.no(entity.getNo())
				.pic(entity.getPic())
				.characterNo(entity.getCharacter().getNo())
				.itemNoList(entity.getItem().stream().map(Item::getNo).collect(Collectors.toList()))
//				.character(Character.toDto(entity.getCharacter()))
//				.item(entity.getItem().stream().map(Item::toDto).collect(Collectors.toList()))
				.build();
	}
}
