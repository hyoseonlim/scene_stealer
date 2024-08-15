package pack.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.Item;
import pack.entity.Style;
import pack.entity.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StyleDto {
	private Integer no;
	private String pic;
	private CharacterDto character;
	private List<ItemDto> item;
	
	public static Style toEntity (StyleDto dto) {
		return Style.builder()
				.no(dto.getNo())
				.pic(dto.getPic())
				.item(dto.getItem().stream().map(ItemDto::toEntity).collect(Collectors.toList()))
				.character(CharacterDto.toEntity(dto.getCharacter()))
				.build();
	}
}
