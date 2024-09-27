package pack.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.NoticeDto;
import pack.dto.UserDto;
import pack.entity.Notice;
import pack.entity.User;
import pack.repository.NoticesRepository;
import pack.repository.UsersRepository;

@Repository
public class UserModel {
	
	@Autowired
	private NoticesRepository nrps;
	
	@Autowired
	private UsersRepository usersRepository;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public Page<NoticeDto> getNoticeList(Pageable pageable) {
		Page<Notice> noticePage = nrps.findAll(pageable);
		return noticePage.map(Notice::toDto);
	}
	
	public NoticeDto getNoticeInfo(int noticeNo) {
		return Notice.toDto(nrps.findById(noticeNo).get());
	}

	// 사용자 정보 조회
	public UserDto getUserByNo(Integer no) {
	    User user = usersRepository.findByNo(no)
	        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	    return User.toDto(user);
	}
	
	public boolean validateCurrentPassword(Integer userNo, String currentPassword) {
	    User user = usersRepository.findById(userNo).orElse(null);
	    if (user != null) {
	        return passwordEncoder.matches(currentPassword, user.getPwd());
	    }
	    return false;
	}

    // 사용자 정보 업데이트
	public boolean updateUser(Integer userNo, UserDto userDto) {
	    User user = usersRepository.findById(userNo).orElse(null);
	    if (user != null) {
	        user.setName(userDto.getName());
	        user.setEmail(userDto.getEmail());
	        user.setTel(userDto.getTel());
	        user.setZipcode(userDto.getZipcode());
	        user.setAddress(userDto.getAddress());

	        // 비밀번호가 null이 아니고, 기존 비밀번호와 다를 경우에만 암호화 및 업데이트
	        if (userDto.getPwd() != null && !userDto.getPwd().isEmpty() && !userDto.getPwd().equals(user.getPwd())) {
	            String encodedPassword = passwordEncoder.encode(userDto.getPwd());
	            user.setPwd(encodedPassword);
	        }

	        usersRepository.save(user);
	        return true;
	    }
	    return false;
	}
	
	@Transactional
	public Boolean deleteUser(Integer userNo) {
		boolean b = false;
		try {
	        User user = usersRepository.findById(userNo).orElse(null);
	        if (user != null) {
	            user.setPwd(null);
	            user.setName(null);
	            user.setTel(null);
	            user.setEmail(null);
	            user.setZipcode(null);
	            user.setAddress(null);
	            user.setNickname(null);
	            user.setBio(null);
	            user.setPic(null);
	            user.setIdK(null);
	            user.setIdN(null);
	            user.setIdG(null);

	            usersRepository.save(user);
	        }
			b = true;
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }

		return b;
	}
	
	// 주문 상태 조회
	public boolean orderStatus(Integer userNo) {
	    List<String> states = usersRepository.findOrderStatesByUserNo(userNo);
	    if (states.isEmpty()) {
	        return true; // 주문이 없는 경우 true 반환
	    }
	    for (String state : states) {
	        if (!"배송완료".equals(state) && !"주문취소".equals(state)) {
	            return false; // "배송완료"나 "주문취소"가 아닌 상태가 있으면 false 반환
	        }
	    }
	    return true; // 모든 상태가 "배송완료"인 경우에만 true 반환
	}
}
