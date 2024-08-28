package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.StyleItem;

public interface StyleItemRepository extends JpaRepository<StyleItem, Integer> {
	public List<StyleItem> findByStyleNo(int no);
	
	// 해당 배역의 전체 아이템 목록
	@Query("SELECT si FROM StyleItem si WHERE si.style.no IN (SELECT s.no FROM Character c JOIN c.styles s WHERE c.no = :no)")
	public List<StyleItem> findByCharacterNo(@Param("no") int no);
	
	public List<StyleItem> findByStyleNoIn(List<Integer> list);
}
