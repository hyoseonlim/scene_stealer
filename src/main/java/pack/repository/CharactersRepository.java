package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Character;

public interface CharactersRepository extends JpaRepository<Character, Integer>{

}
