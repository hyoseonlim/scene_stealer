package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Item;

public interface ItemsRepository extends JpaRepository<Item, Integer>{

	public List<Item> findByStyleNo(int no);
}
