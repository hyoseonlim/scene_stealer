package pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Actor;

public interface ActorsRepository extends JpaRepository<Actor, Integer>{
    @Query("SELECT a FROM Actor a WHERE a.name LIKE %:term%")
    List<Actor> findByNameContaining(@Param("term") String term);
    
    @Query("SELECT a, s FROM Actor a JOIN a.showActors sa JOIN sa.show s WHERE a.name LIKE %:term%")
    List<Object[]> findActorsWithShows(@Param("term") String term);
 
    @Query("SELECT sa.show.no FROM ShowActor sa WHERE sa.actor.no = :actorNo")
    List<Integer> findShowsByActorNo(@Param("actorNo") Integer actorNo);
}
