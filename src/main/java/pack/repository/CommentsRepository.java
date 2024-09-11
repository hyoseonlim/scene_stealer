package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Integer>{

	public List<Comment> findByPostNo(int no);
	
	public int deleteByNo(int no);
	
//	Page<Comment> findByPostNo(int postNo, Pageable pageable);
	
	// 부모 댓글을 페이징으로 가져옴
	Page<Comment> findByPostNoAndParentCommentNoIsNull(int postNo, Pageable pageable);

	// 자식 댓글을 가져옴
	List<Comment> findByParentCommentNo(int parentCommentNo);

	void deleteByPostNo(Integer postNo);
}
