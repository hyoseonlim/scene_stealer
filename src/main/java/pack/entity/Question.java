package pack.entity;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.QuestionDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private String title;
	private String contents;

	@Column(name = "pic")
	private String pic; // URL or file path

	private String answer;

	@ManyToOne
	@JoinColumn(name = "user_no")
	private User user;
	
	public static QuestionDto toDto(Question entity) {
		return QuestionDto.builder()
				.no(entity.getNo())
				.title(entity.getTitle())
				.contents(entity.getContents())
				.pic(entity.getPic())
				.answer(entity.getAnswer())
				.user(User.toDto(entity.getUser()))
				.build();
	}

}
