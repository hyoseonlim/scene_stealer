package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.dto.UserDto;
import pack.entity.Product;
import pack.entity.Review;

public interface ReviewsRepository extends JpaRepository<Review, Integer>{

	public List<Review> findTop3ByOrderByNoDesc();
	public List<Review> findByUserNo(int userNo);// user당 리뷰 불러오기
}
