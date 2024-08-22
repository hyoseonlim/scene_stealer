package pack.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Order;
import pack.entity.Product;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
	Order findByNo(Integer no);

	// 전체 자료 읽기
	Page<Order> findAll(Pageable pageable);

//	// 사용자 ID로 주문 찾기 (부분 문자열)
//	@Query("SELECT o FROM Order o JOIN o.user u WHERE LOWER(u.id) LIKE LOWER(CONCAT('%', :userId, '%'))")
//	Page<Order> findByUserIdContainingIgnoreCase(@Param("userId") String userId, Pageable pageable);
//
//	// 주문 상태로 찾기 (부분 문자열)
//	@Query("SELECT o FROM Order o WHERE LOWER(o.state) LIKE LOWER(CONCAT('%', :state, '%'))")
//	Page<Order> findByStateContainingIgnoreCase(@Param("state") String state, Pageable pageable);

	// 날짜로 주문 찾기
//	@Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate")
//	Page<Order> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
//			Pageable pageable);
	
	@Query("SELECT o FROM Order o JOIN o.user u WHERE (:searchField = 'userId' AND LOWER(u.id) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
	           "OR (:searchField = 'state' AND LOWER(o.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
	           "OR (:searchField = 'date' AND o.date BETWEEN :startDate AND :endDate)")
	    Page<Order> findOrders(@Param("searchTerm") String searchTerm, 
	                           @Param("searchField") String searchField,
	                           @Param("startDate") java.util.Date startDate,
	                           @Param("endDate") java.util.Date endDate,
	                           Pageable pageable);


}
