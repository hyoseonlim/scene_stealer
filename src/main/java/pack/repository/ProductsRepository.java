package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
