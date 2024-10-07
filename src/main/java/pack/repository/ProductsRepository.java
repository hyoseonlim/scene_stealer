package pack.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import pack.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    
    // 전체 자료 읽기
    Page<Product> findAll(Pageable pageable);
    
    // 전체) 판매중
    Page<Product> findAllByAvailableTrueOrderByNoDesc(Pageable pageable);
    
    // 최신순 (PK 내림차순)
    Page<Product> findAllByOrderByNoDesc(Pageable pageable);
    
    // ID로 제품 찾기
    Product findByNo(Integer no);
    
    // 이름 중복 확인 메서드
    boolean existsByName(String name);
    
    // 카테고리로 제품 찾기
    //List<Product> findByCategory(String category);
    Page<Product> findByCategoryOrderByNoDesc(String category, Pageable pageable);
    
    // 카테고리별) 판매중
    Page<Product> findByCategoryAndAvailableIsTrue(String category, Pageable pageable);
    Page<Product> findByCategoryAndAvailableIsTrueOrderByNoDesc(String category, Pageable pageable);
        
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(@Param("name") String name);

    List<Product> findByNameContainingAndAvailableIsTrue(@Param("name") String name);

    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByNameContainingOrderByNoDesc(@Param("name") String name, Pageable pageable);
   

    // 날짜로 제품 찾기 (날짜 형식에 맞게 쿼리 수정)
    // 아래 쿼리는 문자열 형식으로 날짜를 비교하는 방법을 보여줍니다. 실제 사용 시 날짜를 직접 비교하는 것이 좋습니다.
    //@Query("SELECT p FROM Product p WHERE p.date LIKE %:date%")
    //Page<Product> findByDateContainingIgnoreCase(@Param("date") String date, Pageable pageable);
    // 이름으로 검색
    Page<Product> findByNameContainingIgnoreCaseOrderByNoDesc(String name, Pageable pageable);

    // 날짜로 검색
    Page<Product> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 카테고리로 검색
    Page<Product> findByCategoryContainingIgnoreCaseOrderByNoDesc(String category, Pageable pageable);

    // 이름과 날짜로 검색
    Page<Product> findByNameContainingIgnoreCaseAndDateBetweenOrderByNoDesc(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // 이름과 카테고리로 검색
    Page<Product> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCaseOrderByNoDesc(String name, String category, Pageable pageable);

    // 날짜와 카테고리로 검색
    Page<Product> findByDateBetweenAndCategoryContainingIgnoreCaseOrderByNoDesc(LocalDateTime startDate, LocalDateTime endDate, String category, Pageable pageable);

    // 이름, 날짜, 카테고리로 검색
    Page<Product> findByNameContainingIgnoreCaseAndDateBetweenAndCategoryContainingIgnoreCaseOrderByNoDesc(String name, LocalDateTime startDate, LocalDateTime endDate, String category, Pageable pageable);

    

    
    List<Product> findByNoIn(List<Integer> list);
    
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = 0 WHERE p.no = :no")
    void updateStockToZeroByNo(@Param("no") Integer no);
    
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.available = false WHERE p.no = :no")
    void setProductUnavailable(@Param("no") Integer no);

}
