package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import pack.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    Product findByNo(Integer no);
    boolean existsByName(String name);  // 이름 중복 확인 메서드 추가

    public List<Product> findByCategory(String category);
    
    @Query("SELECT a FROM Product a WHERE a.name LIKE %:term%")
    List<Product> findByNameContaining(@Param("term") String term);


}
