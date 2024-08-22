package pack.admin.model;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;
import pack.dto.OrderDto;
import pack.dto.ProductDto;
import pack.entity.Order;
import pack.entity.Product;
import pack.repository.OrdersRepository;
import pack.repository.ProductsRepository;

@Repository
public class AdminOrderModel {

    @Autowired
    private OrdersRepository ordersRepository;
    
    @Autowired
    private ProductsRepository productsRepository;

//    // 모든 주문을 가져오는 메서드
//    public List<OrderDto> getAllOrders() {
//        return ordersRepository.findAll().stream()
//                .map(Order::toDto)
//                .collect(Collectors.toList());
//    }
//    
    public Page<OrderDto> listAll(Pageable pageable) {
        Page<Order> products = ordersRepository.findAll(pageable);
        return products.map(Order::toDto);
    }
    public Page<OrderDto> searchOrders(Pageable pageable, String searchTerm, String searchField, String startDate, String endDate) {
        Page<Order> orders;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        switch (searchField) {
            case "userId":
                orders = ordersRepository.findByUserIdContainingIgnoreCase(searchTerm, pageable);
                break;
            case "state":
                orders = ordersRepository.findByStateContainingIgnoreCase(searchTerm, pageable);
                break;
            case "date":
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
                orders = ordersRepository.findByDateBetween(start, end, pageable);
                break;
            default:
                orders = ordersRepository.findAll(pageable);
                break;
        }

        return orders.map(Order::toDto);
    }	

 // 검색 기능을 추가한 메서드
    public Page<OrderDto> searchOrders(Pageable pageable, String searchTerm, String searchField) {
        Page<Order> orders;
        switch (searchField) {
            case "userId":
                orders = ordersRepository.findByUserIdContainingIgnoreCase(searchTerm, pageable);
                break;
            case "state":
                orders = ordersRepository.findByStateContainingIgnoreCase(searchTerm, pageable);
                break;
//            case "date":
//                orders = ordersRepository.findByDateContainingIgnoreCase(searchTerm, pageable);
//                break;
            default:
            	System.out.println("가나다라마바사\n\n\n\n\n\n\n\n\n\n\n");
                orders = ordersRepository.findAll(pageable);
                break;
        }

        return orders.map(Order::toDto);
    }	
    public OrderDto getData(Integer no) {
    	OrderDto order = Order.toDto(ordersRepository.findByNo(no));
    	return order;
    }
    
    public Map<Integer, String> getProductInfo(List<Integer> productNoList) {
    	Map<Integer, String> productInfo = new HashMap<Integer, String>();
    
    	for (Integer i : productNoList) {
    		String productName = productsRepository.findById(i).get().getName();
    		productInfo.put(i, productName);
    	}
    	
    	return productInfo;
    }

    // 주문 상태를 업데이트하는 메서드
    @Transactional
    public String updateOrderStatus(Integer orderNo, String status) {
        try {
            Order order = ordersRepository.findById(orderNo)
                    .orElseThrow(() -> new IllegalStateException("ID가 " + orderNo + "인 주문을 찾을 수 없습니다."));
        
            order.setState(status);  // 상태 업데이트
            ordersRepository.save(order);  // 변경사항 저장
            return "주문 상태가 성공적으로 업데이트되었습니다.";
        } catch (Exception e) {
            System.err.println("주문 상태 업데이트 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }
   

 
}
