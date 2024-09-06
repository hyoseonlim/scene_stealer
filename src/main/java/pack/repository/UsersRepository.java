package pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
	List<User> findByNicknameContaining(@Param("name") String name);
	
	Optional<User> findByIdKAndNo(String id, int userNo);

	@Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
	Page<User> findByNicknameContaining(@Param("name") String name, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.id LIKE %:id%")
	List<User> findByIdContaining(@Param("id") String id);

	public Page<User> findByNoIn(List<Integer> followInfoList, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.id LIKE %:id%")
	Page<User> findByIdContaining(@Param("id") String id, Pageable pageable);
	
	Optional<User> findByEmail(String email);
	Optional<User> findByIdK(String idk);
	Optional<User> findByIdN(String idn);
	

	// 로그인
//    @Query("SELECT u FROM User u WHERE u.id = :id")
//    Optional<User> findByLoginId(@Param("id") String id);
    
    Optional<User> findById(String id);
    
    Optional<User> findByNo(Integer no);
    
    Optional<User> findByIdAndEmail(String id, String email);
    
    @Query("SELECT o.state FROM Order o WHERE o.user.no = :userNo")
    List<String> findOrderStatesByUserNo(@Param("userNo") Integer userNo);
}
