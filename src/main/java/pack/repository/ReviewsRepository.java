package pack.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.dto.UserDto;
import pack.entity.Product;
import pack.entity.Review;

public interface ReviewsRepository extends JpaRepository<Review, Integer>{

	public List<Review> findTop3ByOrderByNoDesc();
	public Page<Review> findByProductNo(int productNo, Pageable pageable);  // 상품 ID로 리뷰 조회
	int countByProductNo(int productNo); // 상품 ID로 리뷰 수를 세는 메서드
	
	public List<Review> findByUserNo(int userNo);// user당 리뷰 불러오기
	
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.product.no = :no")
	BigDecimal findAverageRatingByProduct(@Param("no") Integer productId);
}
