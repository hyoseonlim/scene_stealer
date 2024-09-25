package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Item;

public interface ItemsRepository extends JpaRepository<Item, Integer>{
	// 아이템명 자동완성
	@Query("SELECT i FROM Item i WHERE i.name LIKE %:name%")
    List<Item> findByNameContaining(@Param("name") String name);
	
	List<Item> findByNoIn(List<Integer> list);
}
