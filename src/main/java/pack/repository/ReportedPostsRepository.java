package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.ReportedPost;

public interface ReportedPostsRepository extends JpaRepository<ReportedPost, Integer>{

	List<ReportedPost> findAllByOrderByNoDesc();
	 List<ReportedPost> findByUser_Id(String userid);
	 void deleteByUser_Id(String userid);
	 
	 void deleteByPostNo(Integer postNo);
}
