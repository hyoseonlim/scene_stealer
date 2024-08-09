package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Actors;

public interface ActorsRepository extends JpaRepository<Actors, Integer>{

}
