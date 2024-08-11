package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer>{

}
