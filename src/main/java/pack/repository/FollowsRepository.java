package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Follow;

public interface FollowsRepository extends JpaRepository<Follow, Integer> {

//	public List<Follow> findByFolloweeNo(int no);
//
//	public List<Follow> findByFollowerNo(int no);
//
	public List<Follow> findByFollowerNoAndFolloweeNo(int no, int fno);
	


	public int deleteByFolloweeNoAndFollowerNo(int fno, int no);
	
	@Query("SELECT f FROM Follow f WHERE f.followee.no = :no AND f.follower.email IS NOT NULL")
	public List<Follow> findByFolloweeNoAndFollowerEmailIsNotNull(@Param("no") int no);

	@Query("SELECT f FROM Follow f WHERE f.follower.no = :no AND f.followee.email IS NOT NULL")
	public List<Follow> findByFollowerNoAndFolloweeEmailIsNotNull(@Param("no") int no);


	
}
