package pack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.AuthEntity;

public interface AuthRepository extends JpaRepository<AuthEntity, Integer> {

	// 로그인
	Optional<AuthEntity> findById(String id);

}
