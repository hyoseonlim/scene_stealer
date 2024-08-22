package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Product;
import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
    @Query("SELECT u FROM User u WHERE u.nickname LIKE %:name%")
    List<User> findByNicknameContaining(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.id LIKE %:id%")
    List<User> findByIdContaining(@Param("id") String id);
}
