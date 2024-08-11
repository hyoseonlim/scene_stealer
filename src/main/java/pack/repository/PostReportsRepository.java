package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.ReportedPosts;

public interface PostReportsRepository extends JpaRepository<ReportedPosts, Integer>{

}
