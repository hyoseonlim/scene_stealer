package pack.entity;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.AdminDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private String id;
	private String passwd;

	public static AdminDto toDto (Admin entity) {
    	return AdminDto.builder()
    			.no(entity.getNo())
    			.id(entity.getId())
    			.passwd(entity.getPasswd())
    			.build();
    }
}
