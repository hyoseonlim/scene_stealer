package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Characters;

public interface CharactersRepository extends JpaRepository<Characters, Integer>{

}
