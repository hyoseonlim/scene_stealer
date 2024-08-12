package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CharacterLikes;

public interface ScrapRepository extends JpaRepository<CharacterLikes, Integer>{

}
