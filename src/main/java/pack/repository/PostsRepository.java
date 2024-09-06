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

	Page<Post> findByReportsCountGreaterThanOrderByNoDesc(int count, Pageable pageable);

	Page<Post> findByReportsCountGreaterThanOrderByReportsCountDesc(int count, Pageable pageable);

	public List<Post> findTop3ByDeletedIsFalseOrderByNoDesc();

	public List<Post> findByUserNo(int no);
	

	@Query("SELECT p FROM Post AS p JOIN p.user AS u WHERE u.no IN :postNoList AND p.reportsCount < 4 ORDER BY p.no DESC")
	Page<Post> findByUserNoIn(@Param("postNoList") List<Integer> postNoList, Pageable pageable);

	public int deleteByNo(int no);

	@Query("SELECT p FROM Post AS p JOIN p.user AS u WHERE u.no = :userNo AND p.reportsCount < 4 ORDER BY p.no DESC")
	Page<Post> findByUserNo(@Param("userNo") int userNo, Pageable pageable);

    // 좋아요 순으로 모든 게시글 가져오기
    Page<Post> findByDeletedIsFalse(Pageable pageable);
	
    // 좋아요 순으로 모든 게시글 가져오기
    Page<Post> findAll(Pageable pageable);
    
    // 소프트 삭제된 게시물 제외 전체 조회
 	@Query("SELECT p FROM Post p WHERE p.deleted = false ORDER BY p.no DESC")
 	List<Post> findAllActive();

 	// 소프트 삭제된 게시물 제외 전체 조회 (페이징)
 	@Query("SELECT p FROM Post p WHERE p.deleted = false ORDER BY p.no DESC")
 	Page<Post> findAllActive(Pageable pageable);

 	// 특정 사용자의 활성 게시물 조회
 	@Query("SELECT p FROM Post p WHERE p.user.no = :userNo AND p.deleted = false ORDER BY p.no DESC")
 	Page<Post> findActiveByUserNo(@Param("userNo") int userNo, Pageable pageable);

 	// 소프트 삭제된 게시물 조회
 	@Query("SELECT p FROM Post p WHERE p.deleted = true ORDER BY p.deletedAt DESC")
 	Page<Post> findDeletedPosts(Pageable pageable);

 	// 특정 사용자의 소프트 삭제된 게시물 조회
 	@Query("SELECT p FROM Post p WHERE p.user.no = :userNo AND p.deleted = true ORDER BY p.deletedAt DESC")
 	Page<Post> findDeletedPostsByUserNo(@Param("userNo") int userNo, Pageable pageable);

 // 완전 삭제를 위한 메서드 수정: no 필드로 삭제
    int deleteByNoAndDeletedTrue(int no);
    Page<Post> findByDeletedFalse(Pageable pageable); // 삭제되지 않은 게시물 가져오기
    Page<Post> findByUserNoInAndDeletedFalse(List<Integer> userNos, Pageable pageable); // 특정 유저들의 삭제되지 않은 게시물 가져오기


}
