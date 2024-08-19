package pack.admin.model;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import pack.dto.OrderDto;
import pack.entity.Order;
import pack.repository.OrdersRepository;

@Repository
public class AdminOrderModel {

    @Autowired
    private OrdersRepository ordersRepository;

    // 모든 주문을 가져오는 메서드
    public List<OrderDto> getAllOrders() {
        return ordersRepository.findAll().stream()
                .map(Order::toDto)
                .collect(Collectors.toList());
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
