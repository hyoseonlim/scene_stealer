package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    Product findByNo(Integer no);
    boolean existsByName(String name);  // 이름 중복 확인 메서드 추가
}
