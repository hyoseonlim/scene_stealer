package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Review;

public interface ReviewsRepository extends JpaRepository<Review, Integer>{

}
