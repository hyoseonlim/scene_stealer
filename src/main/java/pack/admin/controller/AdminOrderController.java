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
    // DELETE 요청을 처리하여 특정 상품 번호(no)의 상품을 삭제
    @DeleteMapping("/{no}")
    public Map<String, Object> deleteProduct(@PathVariable("no") Integer no) {
        Map<String, Object> response = new HashMap<>();  // 응답 데이터를 담을 맵 객체 생성
        String result = adminOrderModel.deleteOrder(no);  // 상품 삭제 로직을 실행하고 결과 메시지를 반환
        if ("주문 삭제 성공".equals(result)) {
            response.put("isSuccess", true);  // 성공 상태와 메시지를 응답에 추가
            response.put("message", result);
        } else {
            response.put("isSuccess", false);  // 실패 상태와 메시지를 응답에 추가
            response.put("message", result);
        }
        return response;  // 응답을 반환
    }

   
}
