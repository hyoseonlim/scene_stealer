package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Review;

public interface ReviewsRepository extends JpaRepository<Review, Integer>{

	public List<Review> findTop3ByOrderByNoDesc();
}
