package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Show;
import pack.entity.ShowActor;

public interface ShowActorRepository extends JpaRepository<ShowActor, Integer>{
	public void deleteByShow(Show show);
}
