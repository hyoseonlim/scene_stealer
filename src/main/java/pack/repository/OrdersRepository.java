package pack.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pack.entity.Order;
import pack.entity.User;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    Order findByNo(Integer no);

    // 전체 자료 읽기
    Page<Order> findAll(Pageable pageable);

    // 주문 상태로 찾기 (부분 문자열)
    @Query("SELECT o FROM Order o WHERE LOWER(o.state) LIKE LOWER(CONCAT('%', :state, '%')) ORDER BY o.no DESC")
    Page<Order> findByStateContainingIgnoreCase(@Param("state") String state, Pageable pageable);

    // 날짜로 주문 찾기
    @Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate ORDER BY o.no DESC")
    Page<Order> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    public List<Order> findByUserNo(Integer no);
    public List<Order> findTop5ByUserNoOrderByNoDesc(Integer no);
    
    public Page<Order> findByNoIn(List<Integer> list, Pageable pageable);
    
    //사용자와 상태로 주문 찾기 (장바구니 확인)
//    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.state = :status")
//    Optional<Order> findByUserAndStatus(@Param("user") User user, @Param("status") String status);
 
    
    
    
    
    // ⭐⭐⭐  통계용  ⭐⭐⭐
 
    // 특정 기간 동안의 총 매출
    @Query("SELECT SUM(o.price) FROM Order o WHERE o.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // 월별 매출
    @Query("SELECT MONTH(o.date) as month, SUM(op.price * op.quantity) as totalRevenue FROM Order o JOIN o.orderProducts op " +
            "WHERE o.date BETWEEN :startDate AND :endDate GROUP BY MONTH(o.date)")
     List<Object[]> findMonthlyRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
