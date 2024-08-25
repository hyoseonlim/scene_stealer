package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer>{
	
	public List<OrderProduct> findByOrderNoIn(List<Integer> orderNoList);
	
	public List<OrderProduct> findByOrderNo(Integer orderNo);
	
}
