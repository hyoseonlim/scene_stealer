package pack.websocket;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.dto.UserDto;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
	
	// 여기서 작업해 보고 user_chat 관계 테이블의 필요성을 재고해 봐야 함.
	// 1. 유저가 볼 수는 없지만 관리자가 채팅 기록을 봐야 함
	// 1-1. 같은 유저가 여러 번 채팅 문의를 했을 경우 별도로 저장해야 하지 않나?
	// 2. 채팅 문의 카테고리를 유저가 채팅 시작시에 선택하거나 관리자가 상담 종료 후 선택함
	// 2-1. 한 건의 상담마다 한 건의 카테고리가 선택되어야 하므로

	private Integer no;

	private UserDto userNo;

	private boolean sendCheck;

	private String content;

	private Date date;

}
