package pack.admin.controller;

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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderModel adminOrderModel;
    
    @Autowired
    private UsersRepository userRepository;
    @GetMapping
    public Page<OrderDto> getOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "searchField", defaultValue = "userId") String searchField,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<OrderDto> orderPage;

        if (searchField == null) {
            orderPage = adminOrderModel.listAll(pageable);
        } else {
            orderPage = adminOrderModel.searchOrders(pageable, searchTerm, searchField, startDate, endDate);
        }

        // 각 주문의 총 수량을 로그로 출력
        orderPage.forEach(order -> {
            int totalQuantity = order.getTotalQuantity();
        });

        return orderPage;
    }

    @PutMapping("/{orderNo}/status")
    public String updateOrderStatus(@PathVariable("orderNo") Integer orderNo,
                                    @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        return adminOrderModel.updateOrderStatus(orderNo, status);
    }

    @GetMapping("/detail/{orderNo}")
    public Map<String, Object> getOrderDetail(@PathVariable("orderNo") Integer orderNo) {
        OrderDto orderDto = adminOrderModel.getData(orderNo);
        Map<Integer, String> productInfo = adminOrderModel.getProductInfo(orderDto.getProductNoList());

        // 각 주문의 총 수량 포함
        Map<String, Object> result = new HashMap<>();
        result.put("order", orderDto);
        result.put("product", productInfo);
        result.put("totalQuantity", orderDto.getTotalQuantity());  // 추가: 총 수량
        result.put("user", User.toDto(userRepository.findById(orderDto.getUserNo()).get()));
        
        return result;
    }
}
