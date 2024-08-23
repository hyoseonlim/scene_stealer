package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Follow;

public interface FollowsRepository extends JpaRepository<Follow, Integer> {

	public List<Follow> findByFolloweeNo(int no);

	public List<Follow> findByFollowerNo(int no);

	public List<Follow> findByFollowerNoAndFolloweeNo(int no, int fno);

	public int deleteByFolloweeNoAndFollowerNo(int fno, int no);
	
}
