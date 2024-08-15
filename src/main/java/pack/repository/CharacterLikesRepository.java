package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CharacterLike;

public interface CharacterLikesRepository extends JpaRepository<CharacterLike, Integer>{
	public String findByCharacterNoAndUserId(int no, String id);
}
