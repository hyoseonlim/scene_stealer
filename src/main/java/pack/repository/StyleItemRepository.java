package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.StyleItem;

public interface StyleItemRepository extends JpaRepository<StyleItem, Integer> {
	public List<StyleItem> findByStyleNo(int no);
}
