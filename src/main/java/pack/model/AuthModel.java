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

    // 회원가입
    public void saveUser(pack.entity.User user) {
        // 비밀번호를 암호화합니다.
        String encodedPassword = passwordEncoder.encode(user.getPwd());

        // 암호화된 비밀번호를 User 엔티티에 설정
        user.setPwd(encodedPassword);
        
        Optional<User> userCheck = usersRepository.findByEmail(user.getEmail());
        if(userCheck.isPresent()) {
        	// 이미 등록된 이메일
        	User preUser = userCheck.get();
        	if(preUser.getId() == null) {
        		preUser.setId(user.getId());
        	}
        	usersRepository.save(preUser);
        } else {
        	// 최초 가입자
        	usersRepository.save(user);

        	CouponUser cu = CouponUser.builder()
                   .user(User.builder().no(user.getNo()).build())
                   .coupon(Coupon.builder().no(1).build())
                   .build();
        	
            curps.save(cu);
        }
    }

    public void saveUserFromDto(UserDto userDto) {
        pack.entity.User user = UserDto.toEntity(userDto);
        saveUser(user);
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
//    
//    
//    // 이메일로 사용자 찾기
//    public User findByEmail(String email) {
//        return usersRepository.findByEmail(email).orElse(null);
//    }
}