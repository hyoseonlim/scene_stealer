package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
	// 전체 유저에게 쿠폰 발급 시 사용
	List<Integer> findAllNos();
}
