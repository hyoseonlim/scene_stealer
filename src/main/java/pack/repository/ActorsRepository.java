package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Actor;

public interface ActorsRepository extends JpaRepository<Actor, Integer>{
	// 자동완성
    @Query("SELECT a FROM Actor a WHERE a.name LIKE %:term%")
    List<Actor> findByNameContaining(@Param("term") String term);

    // 검색 결과 
    @Query("SELECT a, s FROM Actor a JOIN a.showActors sa JOIN sa.show s WHERE a.name LIKE %:term%")
    Page<Object[]> findActorsWithShows(@Param("term") String term, Pageable pageable);
    
    
//    @Query("SELECT a, s FROM Actor a JOIN a.showActors sa JOIN sa.show s WHERE a.name LIKE %:term%")
//    List<Object[]> findActorsWithShows(@Param("term") String term);
 
}
