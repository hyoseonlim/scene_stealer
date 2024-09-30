package pack.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pack.config.CustomUserDetails;
import pack.dto.UserDto;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.User;
import pack.repository.CouponUserRepository;
import pack.repository.UsersRepository;
import pack.service.EmailService;

@Service
public class AuthModel implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CouponUserRepository curps;


    // 로그인
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        pack.entity.User user = usersRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // 사용자 no 값을 추가하여 CustomUserDetails 객체를 반환
        return new CustomUserDetails(
                user.getId(),
                user.getNo(), // user.getNo()는 사용자 no를 반환하는 메소드
                user.getPwd()
        );
    }

    @Autowired
    private EmailService emailService;
    
    // 회원가입
    public void saveUser(UserDto userDto) {
        // 기본 프로필 사진 설정
        userDto.setPic("/images/default.png");
        userDto.setNickname(userDto.getId());

        Optional<User> existingUser = usersRepository.findByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하는 경우 업데이트 처리
            User user = existingUser.get();
            user.setId(userDto.getId());
            user.setName(userDto.getName());
            user.setPwd(passwordEncoder.encode(userDto.getPwd())); // 비밀번호 암호화
            user.setTel(userDto.getTel());
            user.setEmail(userDto.getEmail());
            user.setZipcode(userDto.getZipcode());
            user.setAddress(userDto.getAddress());

            usersRepository.save(user); // 업데이트된 사용자 저장
        } else {
            // 새로운 사용자 등록 처리
            User user = UserDto.toEntity(userDto);
            user.setPwd(passwordEncoder.encode(userDto.getPwd())); // 비밀번호 암호화


            // 새로운 사용자 저장
            usersRepository.save(user);
            
            CouponUser cu = CouponUser.builder()
            		.user(User.builder().no(user.getNo()).build())
            		.coupon(Coupon.builder().no(1).build())
            		.build();
            
            curps.save(cu);

            // 환영 이메일 발송
            emailService.sendWelcomeEmail(user);
        }
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
    
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

}