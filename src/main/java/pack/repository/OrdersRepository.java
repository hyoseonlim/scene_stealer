package pack.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pack.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    
    // 주문 번호로 주문 찾기
    Order findByNo(Integer no);
    
    // 주문 상태와 사용자 이름을 기준으로 검색 (부분 문자열 일치)
    @Query("SELECT o FROM Order o WHERE o.state = :state AND LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%'))")
    Page<Order> findByStateAndUserNameContainingIgnoreCase(
        @Param("state") String state,
        @Param("userName") String userName,
        Pageable pageable
    );

    // 주문 상태로만 검색 (부분 문자열 일치)
    @Query("SELECT o FROM Order o WHERE LOWER(o.state) LIKE LOWER(CONCAT('%', :state, '%')) ORDER BY o.no DESC")
    Page<Order> findByStateContainingIgnoreCase(@Param("state") String state, Pageable pageable);

    // 날짜 범위로 주문 검색
    @Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate ORDER BY o.no DESC")
    Page<Order> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    // 주문자명으로 주문 검색 (부분 문자열 일치)
    @Query("SELECT o FROM Order o WHERE LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%'))")
    Page<Order> findByUserNameContainingIgnoreCase(@Param("userName") String userName, Pageable pageable);

    // 상태와 사용자 이름, 날짜 범위를 모두 사용하여 검색
    @Query("SELECT o FROM Order o WHERE o.state = :state AND LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%')) AND o.date BETWEEN :startDate AND :endDate")
    Page<Order> findByStateAndUserNameContainingIgnoreCaseAndDateBetween(
        @Param("state") String state,
        @Param("userName") String userName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    // 상태와 날짜 범위로 검색
    @Query("SELECT o FROM Order o WHERE o.state = :state AND o.date BETWEEN :startDate AND :endDate ORDER BY o.no DESC")
    Page<Order> findByStateAndDateBetween(
        @Param("state") String state,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    // 사용자 이름과 날짜 범위로 검색
    @Query("SELECT o FROM Order o WHERE LOWER(o.user.name) LIKE LOWER(CONCAT('%', :userName, '%')) AND o.date BETWEEN :startDate AND :endDate ORDER BY o.no DESC")
    Page<Order> findByUserNameContainingIgnoreCaseAndDateBetween(
        @Param("userName") String userName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    // 특정 사용자 번호로 주문 검색
    public List<Order> findByUserNo(Integer no);

    // 특정 사용자 번호로 최근 5개의 주문 검색
    public List<Order> findTop5ByUserNoOrderByNoDesc(Integer no);

    // 특정 주문 번호 리스트로 주문 검색
    public Page<Order> findByNoIn(List<Integer> list, Pageable pageable);

    // ⭐⭐⭐ 통계용 ⭐⭐⭐

    // 특정 기간 동안의 총 매출
    @Query("SELECT SUM(o.price) FROM Order o WHERE o.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 월별 매출
    @Query("SELECT MONTH(o.date) as month, SUM(o.price) as totalRevenue FROM Order o " +
            "WHERE o.date BETWEEN :startDate AND :endDate GROUP BY MONTH(o.date)")
    List<Object[]> findMonthlyRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
