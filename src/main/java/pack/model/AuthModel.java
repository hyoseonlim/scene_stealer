package pack.model;

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

    // 로그인
    public Map<String, Object> login(UserDto userDto) {
        String id = userDto.getId();
        String pwd = userDto.getPwd();
        
        try {
            // 사용자 정보 로드
            CustomUserDetails userDetails = (CustomUserDetails) this.loadUserByUsername(id);
            
            // 비밀번호 검증
            if (!passwordEncoder.matches(pwd, userDetails.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
            }
            
            // JWT 생성
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
            
            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", Map.of(
                    "id", userDetails.getUsername(),
                    "no", userDetails.getNo()
            ));
            response.put("token", jwtToken);
            
            return response;
        } catch (UsernameNotFoundException e) {
            // 사용자를 찾을 수 없을 때의 예외 처리
            throw new IllegalArgumentException("아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    // 회원가입
    public void saveUser(UserDto userDto) {
        setupDefaultValues(userDto);

        Optional<User> existingUser = usersRepository.findByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            // 사용자 이미 존재하면 예외 처리 혹은 적절한 에러 메시지 반환 가능
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        } else {
            // 새로운 사용자 등록 처리
            User user = UserDto.toEntity(userDto);
            user.setPwd(passwordEncoder.encode(userDto.getPwd())); // 비밀번호 암호화
            usersRepository.save(user);

            // 새로운 사용자를 위한 기본 쿠폰 할당
            CouponUser cu = CouponUser.builder()
                .user(User.builder().no(user.getNo()).build())
                .coupon(Coupon.builder().no(1).build())  // 기본 쿠폰 할당
                .build();
            curps.save(cu);
            
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
    }

    // 기본값 설정 메서드
    private void setupDefaultValues(UserDto userDto) {
        userDto.setPic("/images/default.png");
        userDto.setNickname(userDto.getId());
    }

    // 회원가입 아이디 체크
    public boolean idCheck(String id) {
        try {
            return usersRepository.findById(id).isPresent();
        } catch (Exception e) {
            // 예외 처리: 예외를 기록하고 적절한 처리를 합니다.
            System.err.println("Error checking ID: " + e.getMessage());
            throw new RuntimeException("Error checking ID", e);
        }
    }

    // 이메일로 사용자 찾기
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    // 필수 구현 메서드 (UserDetailsService 인터페이스)
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = usersRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return new CustomUserDetails(
            user.getId(),
            user.getNo(), // user.getNo()는 사용자 번호 반환
            user.getPwd()
        );
    }
}
