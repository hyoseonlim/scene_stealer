package pack.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pack.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    
    // 전체 자료 읽기
    Page<Product> findAll(Pageable pageable);
    
    // ID로 제품 찾기
    Product findByNo(Integer no);
    
    // 이름 중복 확인 메서드
    boolean existsByName(String name);
    
    // 카테고리로 제품 찾기
    List<Product> findByCategory(String category);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    // 이름으로 제품 찾기 (부분 문자열)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // 날짜로 제품 찾기 (날짜 형식에 맞게 쿼리 수정)
    // 아래 쿼리는 문자열 형식으로 날짜를 비교하는 방법을 보여줍니다. 실제 사용 시 날짜를 직접 비교하는 것이 좋습니다.
    //@Query("SELECT p FROM Product p WHERE p.date LIKE %:date%")
    //Page<Product> findByDateContainingIgnoreCase(@Param("date") String date, Pageable pageable);

    
    // 카테고리로 제품 찾기 (부분 문자열)
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    Page<Product> findByCategoryContainingIgnoreCase(@Param("category") String category, Pageable pageable);
    
    
 // 날짜로 제품 찾기 (내림차순 정렬 포함)
    @Query("SELECT p FROM Product p WHERE p.date BETWEEN :startDate AND :endDate ORDER BY p.no DESC")
    Page<Product> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    List<Product> findByNoIn(List<Integer> list);
}
