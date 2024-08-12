package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Item;

public interface ItemsRepository extends JpaRepository<Item, Integer>{

}
