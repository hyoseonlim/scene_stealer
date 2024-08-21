package pack.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.admin.model.AdminOrderModel;
import pack.dto.OrderDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderModel adminOrderModel;

    // 모든 주문 목록을 반환하는 엔드포인트
    @GetMapping
    public List<OrderDto> getAllOrders() {
        return adminOrderModel.getAllOrders();
    }

    // 주문 상태를 업데이트하는 엔드포인트
    @PutMapping("/{orderNo}/status")
    public String updateOrderStatus(@PathVariable("orderNo") Integer orderNo, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        return adminOrderModel.updateOrderStatus(orderNo, status);
    }
    @GetMapping("/detail/{orderNo}")
    public Map<String, Object> getOrderDetail(@PathVariable("orderNo") Integer orderNo) {
    	OrderDto orderDto = adminOrderModel.getData(orderNo);
    	Map<Integer, String> productInfo = adminOrderModel.getProductInfo(orderDto.getProductNoList());
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("order", orderDto);
    	result.put("product", productInfo);
    	return result;
    }
   

   
}
