package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CharacterLike;

public interface ScrapRepository extends JpaRepository<CharacterLike, Integer>{

}
