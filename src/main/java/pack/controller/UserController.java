package pack.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.config.CustomUserDetails;
import pack.dto.FindPassDto;
import pack.dto.NoticeDto;
import pack.dto.UserDto;
import pack.model.UserModel;
import pack.service.PasswordResetService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserModel um;

	@Autowired
	private PasswordResetService passwordResetService;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
//    @Autowired
//    private CustomUserDetails userDetailsService;

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
	
	@PostMapping("/user/validate-password")
	public ResponseEntity<Map<String, Boolean>> validateCurrentPassword(@RequestBody UserDto userDto) {
	    try {
	        boolean isValid = um.validateCurrentPassword(userDto.getNo(), userDto.getPwd()); // userDto의 pwd를 현재 비밀번호로 사용
	        return ResponseEntity.ok(Collections.singletonMap("isValid", isValid));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("isValid", false));
	    }
	}

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

	// 회원탈퇴
	@PutMapping("/user/mypage/delete")
	public Map<String, Object> delete(@RequestBody Map<String, Object> requestBody) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // 사용자 번호와 이메일을 안전하게 추출
	        Integer userNo = Integer.parseInt((String) requestBody.get("userNo"));
	        String email = (String) requestBody.get("email");

	        // 사용자 정보 로드
	        UserDto userDto = um.getUserByNo(userNo);
	        
	        // 이메일 비교
	        if (email.equals(userDto.getEmail())) {
	            boolean result = um.deleteUser(userNo);
	            response.put("result", result);
	        } else {
	            response.put("result", false);
	            response.put("message", "이메일이 일치하지 않습니다.");
	        }
	    } catch (Exception e) {
	        response.put("result", false);
	        response.put("message", "오류가 발생했습니다.");
	        e.printStackTrace(); // 실제 배포에서는 스택 트레이스를 로깅으로 대체하는 것이 좋습니다.
	    }

	    return response;
	}
	
	@GetMapping("/user/passwordCheck")
	public ResponseEntity<Boolean> passwordCheck(@RequestParam(name = "no") Integer no) {
	    boolean allCompleted = um.orderStatus(no);
	    
	    return ResponseEntity.ok(allCompleted);
	}
}
