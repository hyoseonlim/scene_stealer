package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Question;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
	private Integer no;

	private String title;
	private String contents;

	private String pic; // URL or file path

	private String answer;

	private UserDto user;
	
	public static Question toEntity(QuestionDto dto) {
		return Question.builder()
				.no(dto.getNo())
				.title(dto.getTitle())
				.contents(dto.getContents())
				.pic(dto.getPic())
				.answer(dto.getAnswer())
				.user(UserDto.toEntity(dto.getUser()))
				.build();
	}
	
	
}
