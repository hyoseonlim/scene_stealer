package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Style;

public interface StylesRepository extends JpaRepository<Style, Integer>{

	public List<Style> findByCharacterNo(int no);
}
