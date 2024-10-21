package pack.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pack.config.CustomUserDetails;
import pack.dto.UserDto;
import pack.entity.Alert;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.User;
import pack.login.JwtUtil;
import pack.repository.AlertsRepository;
import pack.repository.CouponUserRepository;
import pack.repository.UsersRepository;
import pack.service.EmailService;

@Service
public class AuthModel implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CouponUserRepository curps;
    private final AlertsRepository alertRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    // 이메일 인증 코드 저장소
    private Map<String, Integer> emailVerificationCodes = new HashMap<>();
    // 인증 코드 만료 시간 저장소
    private Map<String, LocalDateTime> verificationCodeExpiryTimes = new HashMap<>();

    @Autowired
    public AuthModel(UsersRepository usersRepository, PasswordEncoder passwordEncoder,
                     CouponUserRepository curps, EmailService emailService, JwtUtil jwtUtil, AlertsRepository alertRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.curps = curps;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.alertRepository = alertRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new CustomUserDetails(
                user.getId(),
                user.getNo(),
                user.getPwd()
        );
    }

    public boolean emailCheck(String email) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        return userOptional.isPresent();
    }

    public boolean idCheck(String id) {
        return usersRepository.findById(id).isPresent();
    }

    // 회원가입
    public void saveUser(UserDto userDto) {
        // 사용자 아이디 또는 이메일이 중복인지 체크
        if (usersRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPwd());
        userDto.setPwd(encodedPassword);

        // UserDto를 User 엔티티로 변환 후 저장
        User user = UserDto.toEntity(userDto);
        usersRepository.save(user);

        // 신규 회원에게 기본 쿠폰 제공 (임의의 로직)
        CouponUser couponUser = CouponUser.builder()
                .user(user)
                .coupon(Coupon.builder().no(1).build())  // 기본 쿠폰 번호 1 (필요시 변경)
                .build();
        curps.save(couponUser);

        // 쿠폰 발급 알림 전송
        Alert alert  = new Alert();
        alert.setCategory("프로모션");
        alert.setContent("WELCOME TO SCENE STEALER WORLD! 쿠폰 선물 드려요");
        alert.setUser(user);
        alert.setPath("/user/mypage/coupon");
        alertRepository.save(alert);

        // 환영 이메일 발송
        emailService.sendWelcomeEmail(user);
    }

    // 로그인
    public Map<String, Object> login(UserDto userDto) {
        String id = userDto.getId();
        String pwd = userDto.getPwd();

        CustomUserDetails userDetails = (CustomUserDetails) this.loadUserByUsername(id);
        if (!passwordEncoder.matches(pwd, userDetails.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", Map.of(
                "id", userDetails.getUsername(),
                "no", userDetails.getNo()
        ));
        response.put("token", jwtToken);

        return response;
    }

    // 비밀번호 확인
    public boolean checkPassword(Integer userNo, String password) {
        Optional<User> userOptional = usersRepository.findById(userNo);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        return passwordEncoder.matches(password, user.getPwd());
    }

    // 이메일 인증 코드 발송
    public Map<String, Object> sendVerificationCode(String email) {
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("status", "error");
            response.put("message", "이메일을 입력하세요.");
            return response;
        }

        try {
            String lowerCaseEmail = email.toLowerCase(); // 이메일을 소문자로 변환
            int verificationCode = emailService.sendMail(email); // 인증 코드 생성 및 발송
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10); // 만료 시간 설정

            // 이메일에 대한 인증 코드와 만료 시간 저장
            emailVerificationCodes.put(lowerCaseEmail, verificationCode);
            verificationCodeExpiryTimes.put(lowerCaseEmail, expiryTime);

            response.put("status", "success");
            response.put("message", "인증번호가 이메일로 발송되었습니다.");
            return response;
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "인증번호 발송 실패: " + e.getMessage());
            return response;
        }
    }

    // 이메일 인증 코드 검증
    public Map<String, Object> verifyCode(String email, int inputCode) {
        Map<String, Object> response = new HashMap<>();
        String lowerCaseEmail = email.toLowerCase(); // 이메일을 소문자로 변환

        // 이메일로 저장된 인증 코드와 만료 시간을 가져옴
        Integer storedCode = emailVerificationCodes.get(lowerCaseEmail);
        LocalDateTime expiryTime = verificationCodeExpiryTimes.get(lowerCaseEmail);

        if (storedCode != null && expiryTime != null) {
            if (expiryTime.isBefore(LocalDateTime.now())) {
                response.put("status", "error");
                response.put("message", "인증번호가 만료되었습니다.");
            } else if (storedCode == inputCode) {
                response.put("status", "success");
                response.put("message", "인증이 완료되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "인증번호가 일치하지 않습니다.");
            }
        } else {
            response.put("status", "error");
            response.put("message", "인증번호가 없습니다.");
        }

        return response;
    }

    // JWT 정보 추출
    public String extractUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    public Map<String, Object> getUserInfoByToken(String token, String username) {
        if (username != null && jwtUtil.validateToken(token, username)) {
            UserDetails userDetails = loadUserByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("userNo", ((CustomUserDetails) userDetails).getNo());
            return response;
        } else {
            return Map.of("message", "Invalid token");
        }
    }
}