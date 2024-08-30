package pack.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer>{
	
	public List<OrderProduct> findByOrderNoIn(List<Integer> orderNoList);
	
	public List<OrderProduct> findByOrderNo(Integer orderNo);
	
	@Query("SELECT op FROM OrderProduct op JOIN op.order o WHERE o.user.no = :userNo AND o.state = :state")
	List<OrderProduct> findCartItemsByUserNoAndState(@Param("userNo") int userNo, @Param("state") String state);
	
	// ⭐⭐⭐  통계용  ⭐⭐⭐
	// 기간별 인기상품
	@Query("SELECT op.product.name, SUM(op.quantity) FROM OrderProduct op WHERE op.order.date BETWEEN :startDate AND :endDate GROUP BY op.product.no ORDER BY SUM(op.quantity) DESC")
	List<Object[]> findTopSellingProductsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
