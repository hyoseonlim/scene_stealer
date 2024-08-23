package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pack.entity.Character;
import pack.entity.Show;

public interface CharactersRepository extends JpaRepository<Character, Integer>{

	public List<Character> findByShowNo(int no);
	
	public Page<Character> findByNoIn(List<Integer> userNoList, Pageable pageable);

}
