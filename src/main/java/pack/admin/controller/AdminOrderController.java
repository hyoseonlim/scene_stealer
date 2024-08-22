package pack.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pack.admin.model.AdminOrderModel;
import pack.dto.OrderDto;
import pack.dto.ProductDto;
import pack.entity.Order;
import pack.repository.OrdersRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private AdminOrderModel adminOrderModel;
//
//    // 모든 주문 목록을 반환하는 엔드포인트
//    @GetMapping
//    public List<OrderDto> getAllOrders() {
//        return adminOrderModel.getAllOrders();
//    }

	@GetMapping
	public Page<Order> getOrders(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
			@RequestParam(value = "searchField", defaultValue = "userId") String searchField,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;

		try {
			if (startDate != null && !startDate.isEmpty()) {
				start = sdf.parse(startDate);
			}
			if (endDate != null && !endDate.isEmpty()) {
				end = sdf.parse(endDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Pageable pageable = PageRequest.of(page, size);
		return ordersRepository.findOrders(searchTerm, searchField, start, end, pageable);
	}

	// 주문 상태를 업데이트하는 엔드포인트
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("order", orderDto);
		result.put("product", productInfo);
		return result;
	}

}
