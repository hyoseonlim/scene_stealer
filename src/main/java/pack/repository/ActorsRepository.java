package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Actor;

public interface ActorsRepository extends JpaRepository<Actor, Integer>{
    // 페이징을 지원하지 않는 메서드
    List<Actor> findByNameContaining(String name);

    @Query("SELECT a, s FROM Actor a JOIN a.showActors sa JOIN sa.show s WHERE a.name LIKE %:term%")
    Page<Actor> findByNameContaining(@Param("term") String term, Pageable pageable);
    
    @Query("SELECT a, s FROM Actor a JOIN a.showActors sa JOIN sa.show s WHERE a.name LIKE %:term%")
    List<Object[]> findActorsWithShows(@Param("term") String term);
 
    @Query("SELECT sa.show.no FROM ShowActor sa WHERE sa.actor.no = :actorNo")
    List<Integer> findShowsByActorNo(@Param("actorNo") Integer actorNo);
}
