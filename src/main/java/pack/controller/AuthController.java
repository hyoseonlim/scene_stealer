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
@CrossOrigin(origins = "*") // 모든 출처에서 CORS 요청 허용
public class AuthController {

    @Autowired
    private AuthModel model; // 사용자 인증 관련 작업을 수행하는 모델

	@Autowired
	private UserModel um;

    @Autowired
    private JwtUtil jwtUtil;
    
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
            Optional<User> existingUser = model.findByEmail(userDto.getEmail());

            if (existingUser.isPresent()) {
                // 사용자가 이미 존재하는 경우 업데이트 처리
                User user = existingUser.get();
                
                // userDto의 필드들을 user 엔티티에 덮어쓰기
                user.setId(userDto.getId());
                user.setName(userDto.getName());
                user.setPwd(userDto.getPwd());
                user.setTel(userDto.getTel());
                user.setEmail(userDto.getEmail());
                user.setZipcode(userDto.getZipcode());
                user.setAddress(userDto.getAddress());

                model.saveUser(user); // 업데이트된 사용자 저장
                response.put("status", "success");
                response.put("message", "회원 정보 업데이트 성공");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // 새로운 사용자 등록 처리
                User user = UserDto.toEntity(userDto);
                String encodedPwd = passwordEncoder.encode(userDto.getPwd());
                user.setPwd(encodedPwd);

                model.saveUser(user);

                // 환영 이메일 발송
                emailService.sendWelcomeEmail(user);

                response.put("status", "success");
                response.put("message", "회원가입 성공");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "처리 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 로그인 처리 메소드
    @PostMapping("/user/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        String id = userDto.getId();
        String pwd = userDto.getPwd();

        Map<String, Object> response = new HashMap<>();
        
        try {
            // 사용자 정보 로드
            UserDetails userDetails = model.loadUserByUsername(id);
            
            // 비밀번호 검증
            if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
                // JWT 생성
                String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                
                // 응답에 JWT 포함
                response.put("success", true);
                response.put("user", Map.of(
                        "id", customUserDetails.getUsername(), 
                        "no", customUserDetails.getNo() // 사용자 no 반환
                    ));
                response.put("token", jwtToken);
                return ResponseEntity.ok(response);
            } else {
                // 비밀번호가 틀렸을 때
                response.put("success", false);
                response.put("message", "비밀번호를 확인해주세요."); // 적절한 오류 메시지
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (UsernameNotFoundException e) {
            // 사용자 정보가 없을 때
            response.put("success", false);
            response.put("message", "계정이 존재하지 않습니다."); // 사용자 계정이 없을 때 메시지
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            // 기타 예외 처리
            response.put("success", false);
            response.put("message", "로그인에 실패했습니다."); // 일반적인 로그인 실패 메시지
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

    // 비밀번호 확인
    @PostMapping("/user/passwordCheck")
    public ResponseEntity<Map<String, Object>> passwordCheck(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        Integer userNo = Integer.parseInt((String) requestBody.get("userNo"));
        String password = (String) requestBody.get("pwd");

        try {
            // 사용자 정보 로드
            UserDto userDto = um.getUserByNo(userNo);
            // 암호화된 비밀번호와 입력된 비밀번호 비교
            if (passwordEncoder.matches(password, userDto.getPwd())) {
                response.put("result", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("result", false);
                response.put("message", "비밀번호가 일치하지 않습니다.");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("result", false);
            response.put("message", "오류가 발생했습니다.");
            e.printStackTrace();
            return ResponseEntity.status(500).body(response);
        }
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
    
    @GetMapping("/user/emailCheck")
    public ResponseEntity<Map<String, Object>> emailCheck(@RequestParam(name = "email") String email) {
        System.out.println("수신된 이메일: " + email);  // 수신된 이메일 확인
        
        Optional<User> userOptional = model.findByEmail(email);
        boolean exists = userOptional.isPresent();
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        
        if(exists) {
        	User user = userOptional.get();
        	response.put("id_n", user.getIdN() != null);
            response.put("id_k", user.getIdK() != null);
            response.put("id_g", user.getIdG() != null);
            
            response.put("name", user.getName());
            response.put("tel", user.getTel());
            response.put("zipcode", user.getZipcode());
            response.put("addrStart", user.getAddress());
//            response.put("addrEnd", user.getAddrEnd());
        }
        
        return ResponseEntity.ok(response);
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
    
    // JWT로 정보 얻기
    @GetMapping("/user/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Unauthorized"));
        }

        try {
            token = token.substring(7); // Remove "Bearer " prefix
            String username = jwtUtil.extractUsername(token);

            if (username != null && jwtUtil.validateToken(token, username)) {
                UserDetails userDetails = model.loadUserByUsername(username);
                Map<String, Object> response = new HashMap<>();
                response.put("userNo", ((CustomUserDetails) userDetails).getNo());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid token"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Server error"));
        }
    }
}