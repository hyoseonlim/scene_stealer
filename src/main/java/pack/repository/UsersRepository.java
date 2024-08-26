package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
	List<User> findByNicknameContaining(@Param("name") String name);

	@Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
	Page<User> findByNicknameContaining(@Param("name") String name, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.id LIKE %:id%")
	List<User> findByIdContaining(@Param("id") String id);

	public Page<User> findByNoIn(List<Integer> followInfoList, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.id LIKE %:id%")
	Page<User> findByIdContaining(@Param("id") String id, Pageable pageable);
	
	// 로그인
	User findByIdAndPwd(String id, String pwd);

}
