package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Items;

public interface ItemsRepository extends JpaRepository<Items, Integer>{

}
