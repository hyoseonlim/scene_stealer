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
	
	// 인기상품
	@Query("SELECT op.product.no as no, op.product.name as name, SUM(op.quantity) as quantity FROM OrderProduct op GROUP BY op.product.no ORDER BY SUM(op.quantity) DESC")
	List<Object[]> findTopSellingProducts(Pageable pageable);
	
	
	// 기간별 인기상품
	@Query("SELECT op.product.no as no, op.product.name as name, SUM(op.quantity) as quantity FROM OrderProduct op WHERE op.order.date BETWEEN :startDate AND :endDate GROUP BY op.product.no ORDER BY SUM(op.quantity) DESC")
	List<Object[]> findTopSellingProductsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

	// 반품 비율
	@Query("SELECT p.no AS productNo, p.name AS productName, " +
		       "SUM(CASE WHEN o.state = '배송완료' THEN op.quantity ELSE 0 END) AS deliveredQuantity, " +
		       "SUM(CASE WHEN o.state = '주문취소' THEN op.quantity ELSE 0 END) AS canceledQuantity, " +
		       "(SUM(CASE WHEN o.state = '주문취소' THEN op.quantity ELSE 0 END) * 1.0 / " +
		       "(SUM(CASE WHEN o.state IN ('배송완료', '주문취소') THEN op.quantity ELSE 0 END))) AS returnRate " +
		       "FROM OrderProduct op " +
		       "JOIN op.product p " +
		       "JOIN op.order o " +
		       "WHERE o.state IN ('배송완료', '주문취소') " +
		       "GROUP BY p.no, p.name " +
		       "ORDER BY returnRate DESC")
	    List<Object[]> findProductReturnRates(Pageable pageable);
	
}
