package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.ReportedPost;

public interface ReportedPostsRepository extends JpaRepository<ReportedPost, Integer>{

}
