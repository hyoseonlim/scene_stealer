package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pack.entity.Show;

public interface ShowsRepository extends JpaRepository<Show, Integer>{

	@Query("SELECT s FROM Show AS s JOIN Character AS c ON s.no = c.show.no GROUP BY s.title ORDER BY SUM(c.likesCount) DESC")
	public List<Show> findShowAll();
}
