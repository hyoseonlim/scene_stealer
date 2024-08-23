package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.ReportedPost;

public interface ReportedPostsRepository extends JpaRepository<ReportedPost, Integer>{
	List<ReportedPost> findAll();
}
