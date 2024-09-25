package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer>{
	
	public List<CommentLike> findByCommentNo(int no);
	
	public int deleteByCommentNoAndUserNo(int commentNo, int userNo);
	
	public List<CommentLike> findByCommentNoAndUserNo(int commentNo, int userNo);
	
	void deleteByCommentNo(Integer no);
}
