package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Actor;

public interface ActorsRepository extends JpaRepository<Actor, Integer>{

}
