package pack.admin.model;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import pack.dto.OrderDto;
import pack.entity.Alert;
import pack.entity.Order;
import pack.repository.AlertsRepository;
import pack.repository.OrdersRepository;
import pack.repository.ProductsRepository;
import pack.repository.UsersRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminOrderModel {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private AlertsRepository alertRepository;

    // 검색 필터에 맞는 주문을 찾는 메소드
    public Page<OrderDto> searchOrders(Pageable pageable, String searchTerm, String searchField, String status, String startDate, String endDate) {
        Page<Order> orders;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = null;
        LocalDateTime end = null;

        try {
            if (startDate != null && !startDate.isEmpty()) {
                start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
            }
            if (endDate != null && !endDate.isEmpty()) {
                end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. 형식은 'yyyy-MM-dd'입니다.");
        }

        // 다양한 조건에 맞는 검색 로직
        if (!status.isEmpty() && !searchTerm.isEmpty() && start != null && end != null) {
            orders = ordersRepository.findByStateAndUserNameContainingIgnoreCaseAndDateBetween(status, searchTerm, start, end, pageable);
        } else if (!status.isEmpty() && !searchTerm.isEmpty()) {
            orders = ordersRepository.findByStateAndUserNameContainingIgnoreCase(status, searchTerm, pageable);
        } else if (!status.isEmpty() && start != null && end != null) {
            orders = ordersRepository.findByStateAndDateBetween(status, start, end, pageable);
        } else if (!searchTerm.isEmpty() && start != null && end != null) {
            orders = ordersRepository.findByUserNameContainingIgnoreCaseAndDateBetween(searchTerm, start, end, pageable);
        } else if (!status.isEmpty()) {
            orders = ordersRepository.findByStateContainingIgnoreCase(status, pageable);
        } else if (!searchTerm.isEmpty()) {
            orders = ordersRepository.findByUserNameContainingIgnoreCase(searchTerm, pageable);
        } else if (start != null && end != null) {
            orders = ordersRepository.findByDateBetween(start, end, pageable);
        } else {
            orders = ordersRepository.findAll(pageable);
        }

        return orders.map(Order::toDto);
    }
    public OrderDto getData(Integer no) {
        return Order.toDto(ordersRepository.findByNo(no));
    }
    // 주문 상태 업데이트 메소드
    @Transactional
    public String updateOrderStatus(Integer orderNo, String status) {
        Order order = ordersRepository.findById(orderNo)
                .orElseThrow(() -> new IllegalStateException("ID가 " + orderNo + "인 주문을 찾을 수 없습니다."));
        order.setState(status);
        ordersRepository.save(order);
        // 유저에게 주문 상태 변경 알림
        Alert alert = new Alert();
        alert.setUser(order.getUser());
        alert.setCategory("주문");
        alert.setContent("주문 상태가 <" + status +"> (으)로 변경되었습니다.");
        alert.setPath("/user/mypage/order/" + orderNo);
        alert.setIsRead(false);
        alertRepository.save(alert);
        return "주문 상태가 성공적으로 업데이트되었습니다.";
    }

    // 주문 상세 정보를 반환하는 메소드
    public Map<String, Object> getOrderDetail(Integer orderNo) {
        OrderDto orderDto = Order.toDto(ordersRepository.findByNo(orderNo));
        Map<Integer, String> productInfo = getProductInfo(orderDto.getProductNoList());

        Map<String, Object> result = new HashMap<>();
        result.put("order", orderDto);
        result.put("product", productInfo);
        result.put("user", usersRepository.findById(orderDto.getUserNo()).orElse(null));

        return result;
    }

    // 상품 정보 반환 메소드
    public Map<Integer, String> getProductInfo(List<Integer> productNoList) {
        Map<Integer, String> productInfo = new HashMap<>();
        for (Integer productNo : productNoList) {
            productInfo.put(productNo, productsRepository.findById(productNo)
                    .map(product -> product.getName())
                    .orElse("Unknown Product"));
        }
        return productInfo;
    }
}
