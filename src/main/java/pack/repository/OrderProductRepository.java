package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer>{
	
}
