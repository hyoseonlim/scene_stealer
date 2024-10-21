package pack.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import pack.config.CustomUserDetails;
import pack.dto.UserDto;
import pack.entity.User;
import pack.login.JwtUtil;
import pack.model.AuthModel;
import pack.model.UserModel;
import pack.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 모든 출처에서 CORS 요청 허용
public class AuthController {

    @Autowired
    private AuthModel authModel;

    // 이메일 중복 체크
    @GetMapping("/user/emailCheck")
    public ResponseEntity<Map<String, Object>> emailCheck(@RequestParam(name = "email") String email) {
        boolean exists = authModel.emailCheck(email);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/auth/check")
    public ResponseEntity<Map<String, Object>> idCheck(@RequestParam(name = "id") String id) {
        boolean exists = authModel.idCheck(id);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping(value = "/user/auth/register", produces = "application/json; charset=utf8")
    public ResponseEntity<Void> signup(@RequestBody UserDto userDto) {
        authModel.saveUser(userDto);
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/user/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        try {
            Map<String, Object> response = authModel.login(userDto);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "사용자를 찾을 수 없습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "로그인에 실패했습니다."));
        }
    }

    // 비밀번호 확인
    @PostMapping("/user/passwordCheck")
    public ResponseEntity<Map<String, Object>> passwordCheck(@RequestBody Map<String, Object> requestBody) {
        Integer userNo = Integer.parseInt((String) requestBody.get("userNo"));
        String password = (String) requestBody.get("pwd");

        try {
            boolean isMatch = authModel.checkPassword(userNo, password);
            if (isMatch) {
                return ResponseEntity.ok(Map.of("result", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("result", false, "message", "비밀번호가 일치하지 않습니다."));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", false, "message", "사용자를 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", false, "message", "오류가 발생했습니다."));
        }
    }

    // 이메일 인증 코드 발송
    @PostMapping("/user/auth/send-verification-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = authModel.sendVerificationCode(email);
        return ResponseEntity.ok(response);
    }

    // 이메일 인증 코드 검증
    @PostMapping("/user/auth/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        int inputCode;
        try {
            inputCode = Integer.parseInt(request.get("code"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "유효하지 않은 인증 코드입니다."));
        }
        Map<String, Object> response = authModel.verifyCode(email, inputCode);
        return ResponseEntity.ok(response);
    }

    // JWT로 정보 얻기
    @GetMapping("/user/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized"));
        }

        try {
            token = token.substring(7);
            String username = authModel.extractUsernameFromToken(token);
            Map<String, Object> response = authModel.getUserInfoByToken(token, username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error"));
        }
    }
}