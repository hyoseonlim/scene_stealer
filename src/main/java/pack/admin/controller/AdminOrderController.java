package pack.admin.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pack.admin.model.AdminOrderModel;
import pack.dto.OrderDto;
import pack.entity.User;
import pack.repository.UsersRepository;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderModel adminOrderModel;
    @Autowired
    private UsersRepository userRepository;

    // 주문 목록을 가져오는 메소드
    @GetMapping
    public Page<OrderDto> getOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "searchField", defaultValue = "userName") String searchField,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<OrderDto> orderPage = adminOrderModel.searchOrders(pageable, searchTerm, searchField, status, startDate, endDate);

        return orderPage;
    }

    // 주문 상태를 업데이트하는 메소드
    @PutMapping("/{orderNo}/status")
    public String updateOrderStatus(@PathVariable("orderNo") Integer orderNo,
                                    @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        return adminOrderModel.updateOrderStatus(orderNo, status);
    }

    // 주문 상세 정보를 가져오는 메소드
    @GetMapping("/detail/{orderNo}")
    public Map<String, Object> getOrderDetail(@PathVariable("orderNo") Integer orderNo) {
        // 주문 정보를 가져옵니다.
        OrderDto orderDto = adminOrderModel.getData(orderNo);
        
        // 상품 정보를 가져옵니다.
        Map<Integer, String> productInfo = adminOrderModel.getProductInfo(orderDto.getProductNoList());
        
        // 유저 정보를 가져옵니다.
        User user = userRepository.findById(orderDto.getUserNo()).orElse(null);

        // 각 주문의 총 수량 포함 (추가된 부분)
        Map<String, Object> result = new HashMap<>();
        result.put("order", orderDto);
        result.put("product", productInfo);
        result.put("totalQuantity", orderDto.getTotalQuantity());  // 총 수량 추가
        result.put("user", user != null ? User.toDto(user) : null);  // 유저 정보가 있을 경우 반환

        return result;
    }
}
