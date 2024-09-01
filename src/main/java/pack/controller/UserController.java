package pack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.FindPassDto;
import pack.dto.NoticeDto;
import pack.dto.UserDto;
import pack.model.UserModel;
import pack.service.PasswordResetService;

@RestController
public class UserController {

	@Autowired
	private UserModel um;

	@Autowired
	private PasswordResetService passwordResetService;

	@GetMapping("/user/notice")
	public ResponseEntity<Page<NoticeDto>> getNoticeList(Pageable pageable) {
		Page<NoticeDto> noticePage = um.getNoticeList(pageable);
		return ResponseEntity.ok(noticePage);
	}

	@GetMapping("/user/notice/{noticeNo}")
	public NoticeDto getNoticeInfo(@PathVariable("noticeNo") int noticeNo) {
		return um.getNoticeInfo(noticeNo);
	}

	@GetMapping("/user/update/{no}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("no") Integer no) {
		UserDto userDto = um.getUserByNo(no);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/user/update/{no}")
	public ResponseEntity<String> updateUser(@PathVariable("no") Integer userNo, @RequestBody UserDto userDto) {
		try {
			// 데이터 검증 및 업데이트 로직
			if (userNo.equals(userDto.getNo())) {
				boolean isUpdated = um.updateUser(userNo, userDto);
				if (isUpdated) {
					return ResponseEntity.ok("회원 정보가 수정되었습니다.");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
				}

			} else {
				return ResponseEntity.badRequest().body("유효하지 않은 사용자 번호입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace(); // 예외 스택 트레이스 기록
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}
//	public ResponseEntity<String> updateUser(@PathVariable("no") Integer userNo, @RequestBody UserDto userDto) {
//	    try {
//	        // 데이터 검증 및 업데이트 로직
//	        if (userNo.equals(userDto.getNo())) {
//	        	if (userDto.getPic() == null || userDto.getPic().isEmpty()) userDto.setPic("/images/default.png"); // 이미지가 업로드되지 않았고, 사용자가 기존 이미지가 없는 경우 기본 이미지 설정
//	            boolean isUpdated = um.updateUser(userDto);
//	            
//	            if (isUpdated) {
//	                return ResponseEntity.ok("회원 정보가 수정되었습니다.");
//	            } else {
//	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
//	            }
//	        } else {
//	            return ResponseEntity.badRequest().body("유효하지 않은 사용자 번호입니다.");
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace(); // 예외 스택 트레이스 기록
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
//	    }
//	}

	@PostMapping("/password-reset")
	public ResponseEntity<String> resetPassword(@RequestBody FindPassDto findPassDto) {
		try {
			passwordResetService.resetPassword(findPassDto);
			return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 요청에 실패했습니다.");
		}
	}
}
