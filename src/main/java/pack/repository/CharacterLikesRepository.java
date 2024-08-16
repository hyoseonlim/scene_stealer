package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CharacterLike;

public interface CharacterLikesRepository extends JpaRepository<CharacterLike, Integer>{
	
	public List<CharacterLike> findByCharacterNoAndUserNo(int cno, int uno);
	public List<CharacterLike> findByUserNo(int no);
	
	public int deleteByCharacterNoAndUserNo(int cno, int uno);
}
