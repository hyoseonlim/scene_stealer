package pack.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.UserDto;
import pack.entity.User;
import pack.repository.UsersRepository;

@Repository
public class AuthModel {
    @Autowired
    private UsersRepository userRepository;

    public UserDto login(String id, String pwd) {
        // 사용자 ID와 비밀번호로 데이터베이스에서 사용자 조회
        return User.toDto(userRepository.findByIdAndPwd(id, pwd));
    }
}