package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Post;

public interface PostsRepository extends JpaRepository<Post, Integer> {

	List<Post> findAll();

	List<Post> findByReportsCountGreaterThanOrderByReportsCountDesc(int count);

	Page<Post> findByReportsCountGreaterThan(int count, Pageable pageable);

	Page<Post> findByReportsCountGreaterThanOrderByReportsCountDesc(int count, Pageable pageable);

	public List<Post> findTop3ByOrderByNoDesc();

	public List<Post> findByUserNo(int no);

	@Query("SELECT p FROM Post AS p JOIN p.user AS u WHERE u.no IN :postNoList AND p.reportsCount < 4")
	Page<Post> findByUserNoIn(@Param("postNoList") List<Integer> postNoList, Pageable pageable);

	public int deleteByNo(int no);

	@Query("SELECT p FROM Post AS p JOIN p.user AS u WHERE u.no = :userNo AND p.reportsCount < 4")
	Page<Post> findByUserNo(@Param("userNo") int userNo, Pageable pageable);


}
