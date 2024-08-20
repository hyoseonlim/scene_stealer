package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Integer>{
	Order findByNo(Integer no);
}
