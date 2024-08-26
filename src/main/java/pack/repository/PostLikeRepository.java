package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer>{

	public List<PostLike> findByPostNo(int no);
	
	public int deleteByPostNoAndUserNo(int postNo, int userNo);
	
	public List<PostLike> findByPostNoAndUserNo(int postNo, int userNo);
	
	void deleteByPostNo(Integer postNo);
}
