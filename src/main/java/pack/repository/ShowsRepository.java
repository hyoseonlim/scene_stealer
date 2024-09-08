package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Actor;
import pack.entity.Character;
import pack.entity.Show;

public interface ShowsRepository extends JpaRepository<Show, Integer>{

	@Query("SELECT s FROM Show AS s JOIN Character AS c ON s.no = c.show.no GROUP BY s.title ORDER BY SUM(c.likesCount) DESC")
	public List<Show> findShowAll(Pageable pageable);
	
	@Query("SELECT s FROM Show AS s JOIN Character AS c ON s.no = c.show.no GROUP BY s.title ORDER BY SUM(c.likesCount) DESC")
	public Page<Show> findAll(Pageable pageable);
//	
    @Query("SELECT a FROM Show a WHERE a.title LIKE %:term%")
    List<Show> findByTitleContaining(@Param("term") String term);
//
    // 제목에 검색어를 포함하는 쇼를 페이징 처리하여 반환
    @Query("SELECT s FROM Show s WHERE s.title LIKE %:term%")
    Page<Show> findByTitleContaining(@Param("term") String term, Pageable pageable);
//	
    @Query("SELECT s FROM Show s WHERE s.no IN :showNos")
    List<Show> findByShowNos(@Param("showNos") List<Integer> showNos);
    
//	@Query("SELECT c FROM Character c JOIN c.show s JOIN c.styles st JOIN st.items i WHERE s.no = :showNo")
//	public List<Character> findByTitleContaining(int showNo);

    // 제목에 검색어를 포함하는 쇼와 관련된 캐릭터들을 페이징 처리하여 반환
    @Query("SELECT c FROM Character c JOIN c.show s WHERE s.title LIKE %:term%")
    Page<Character> findCharactersByShowTitle(@Param("term") String term, Pageable pageable);

    
    @Query("SELECT s FROM Show s JOIN s.showActor sa WHERE sa.actor.no = :actorNo")
    List<Show> findShowsByActorNo(@Param("actorNo") Integer actorNo);
    
}
