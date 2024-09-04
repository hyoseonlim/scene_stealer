package pack.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.config.CustomUserDetails;
import pack.dto.UserDto;
import pack.entity.User;
import pack.model.AuthModel;
import pack.service.EmailService;

@RestController
@CrossOrigin(origins = "*") // 모든 출처에서 CORS 요청 허용
public class AuthController {

    @Autowired
    private AuthModel model; // 사용자 인증 관련 작업을 수행하는 모델

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화 및 검증을 위한 PasswordEncoder

    @Autowired
    private EmailService emailService;
    
    // 이메일 인증 코드 저장
    private Map<String, VerificationCode> emailVerificationCodes = new HashMap<>();

    // VerificationCode 클래스 정의
    public static class VerificationCode {
        private int code;
        private LocalDateTime expiryTime;

        public VerificationCode(int code, LocalDateTime expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }

        public int getCode() {
            return code;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }

    // 회원가입 처리
    @PostMapping("/user/auth/register")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        userDto.setPic("/images/default.png");  // 기본 이미지 경로
        userDto.setNickname(userDto.getId());
        
        try {
        	
        	
            // UserDto를 User 엔티티로 변환
            User user = UserDto.toEntity(userDto);
            
            // 사용자 정보를 데이터베이스에 저장
            model.saveUser(user);
            
            // 환영 이메일 발송
            emailService.sendWelcomeEmail(user);
            
            // 성공 응답 구성
            response.put("status", "success");
            response.put("message", "회원가입 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            // 예외 발생 시 실패 응답 구성
            response.put("status", "error");
            response.put("message", "회원가입 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 처리 메소드
    @PostMapping("/user/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        String id = userDto.getId(); // 사용자 ID
        String pwd = userDto.getPwd(); // 사용자 비밀번호

        Map<String, Object> response = new HashMap<>();
        
        UserDetails userDetails;
        
        try {
            // ID를 기준으로 사용자 정보를 로드
            userDetails = model.loadUserByUsername(id);
        } catch (UsernameNotFoundException e) {
            // 사용자 정보가 없는 경우: 401 Unauthorized 응답 반환
            response.put("success", false);
            response.put("message", "계정이 존재하지 않습니다."); // 사용자 없음 메시지
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 비밀번호가 일치하는지 확인
        if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            response.put("success", true);
            response.put("user", Map.of(
                "id", customUserDetails.getUsername(), 
                "no", customUserDetails.getNo() // 사용자 no 반환
            ));
            return ResponseEntity.ok(response);
        } else {
            // 비밀번호가 일치하지 않는 경우: 401 Unauthorized 응답 반환
            response.put("success", false);
            response.put("message", "비밀번호를 확인해주세요."); // 비밀번호 불일치 메시지
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @GetMapping("/user/auth/check")
    public ResponseEntity<Map<String, Object>> idCheck(@RequestParam(name = "id") String id) {
        boolean exists = model.idCheck(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        
        return ResponseEntity.ok(response);
    }

    // 이메일 인증 코드 발송
    @PostMapping("/user/auth/send-verification-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();
        
        if (email == null || email.isEmpty()) {
            response.put("status", "error");
            response.put("message", "이메일을 입력하세요.");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            int verificationCode = emailService.sendMail(email); // 인증 코드 발송
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10); // 10분 유효 시간 설정
            //LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(10); // 10분 유효 시간 설정
            emailVerificationCodes.put(email, new VerificationCode(verificationCode, expiryTime)); // 인증 코드와 만료 시간 저장
            System.out.println("발송된 인증번호: " + verificationCode); // 로그 추가
            response.put("status", "success");
            response.put("message", "인증번호가 이메일로 발송되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "인증번호 발송 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 이메일 인증 코드 검증
    @PostMapping("/user/auth/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        int inputCode;
        Map<String, Object> response = new HashMap<>();

        // 입력된 인증 코드 검증
        try {
            inputCode = Integer.parseInt(request.get("code"));
        } catch (NumberFormatException e) {
            response.put("status", "error");
            response.put("message", "유효하지 않은 인증 코드입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 저장된 인증 코드와 비교
        VerificationCode storedCode = emailVerificationCodes.get(email);

        // 만료 시간 확인 및 코드 비교
        if (storedCode != null) {
            if (storedCode.getExpiryTime().isBefore(LocalDateTime.now())) {
                response.put("status", "error");
                response.put("message", "인증번호가 만료되었습니다.");
                return ResponseEntity.badRequest().body(response);
            } else if (storedCode.getCode() == inputCode) {
                // 인증 성공
                response.put("status", "success");
                response.put("message", "인증이 완료되었습니다.");
                return ResponseEntity.ok(response);
            }
        }

        response.put("status", "error");
        response.put("message", "인증번호가 일치하지 않습니다");
        return ResponseEntity.badRequest().body(response);
    }
}