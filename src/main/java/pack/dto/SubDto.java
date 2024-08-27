package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubDto {
	private ShowDto show;
	private List<CharacterDto> characters;
	private List<StyleDto> styles;
	private List<StyleItemDto> styleItems;
	private List<ItemDto> items;
}
