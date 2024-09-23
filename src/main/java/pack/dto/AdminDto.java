package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Admin;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
	private Integer no;

	private String id;
	private String passwd;
	
	public static Admin toEntity(AdminDto dto) {
		return Admin.builder()
				.no(dto.getNo())
				.id(dto.getId())
				.passwd(dto.getPasswd())
				.build();
	}
}
