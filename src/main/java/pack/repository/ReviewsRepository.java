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
	
	@Query("SELECT r FROM Review r WHERE r.orderProduct.product.no = :productNo")
	Page<Review> findByProduct(@Param("productNo") Integer productNo, Pageable pageable); //상품 번호로 리뷰 조회
	
	@Query("SELECT COUNT(r) FROM Review r WHERE r.orderProduct.product.no = :productNo")
	int countByProduct(@Param("productNo") Integer productNo); // 상품 ID로 리뷰 수를 세는 메서드
	
	public Page<Review> findByUserNo(int userNo, Pageable pageable);// user당 리뷰 불러오기
	//	public Page<Review> findByUserNo(int userNo);// user당 리뷰 불러오기
	
	// 특정 상품에 대한 평균 평점 조회
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.orderProduct.product.no = :productNo")
    public BigDecimal findAverageRatingByProduct(@Param("productNo") Integer productNo);
}
