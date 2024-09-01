package pack.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

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

    // 사용자 정보 업데이트
	public boolean updateUser(Integer userNo, UserDto userDto) {
	    User user = usersRepository.findById(userNo).orElse(null);
	    if (user != null) {
	        user.setName(userDto.getName());
	        user.setEmail(userDto.getEmail());
	        user.setTel(userDto.getTel());
	        user.setZipcode(userDto.getZipcode());
	        user.setAddress(userDto.getAddress());
	        System.out.println("" + userDto.getPwd());
	        System.out.println(user.getPwd());
	        
	        // 비밀번호가 있는 경우에만 암호화
	        if (!user.getPwd().equals(userDto.getPwd())) {
	            String encodedPassword = passwordEncoder.encode(userDto.getPwd());
	            System.out.println(encodedPassword);
	            user.setPwd(encodedPassword);
	        }
	        else {
//	             비밀번호가 없으면 기존 비밀번호 유지
//	             (기존 비밀번호를 유지하기 위해서 아무 것도 하지 않음)
	        	user.setPwd(user.getPwd());
	        }

	        usersRepository.save(user);
	        return true;
	    }
	    return false;
	}
}
