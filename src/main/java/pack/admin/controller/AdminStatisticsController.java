package pack.admin.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.repository.OrderProductRepository;
import pack.repository.OrdersRepository;

@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {
	@Autowired OrdersRepository orderRepository;
	@Autowired OrderProductRepository orderProductRepository;
	
	@GetMapping("/orders/total-revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        BigDecimal totalRevenue = orderRepository.findTotalRevenueBetween(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }
	
	@GetMapping("/orders/monthly-revenue")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        List<Object[]> results = orderRepository.findMonthlyRevenueBetween(startDate, endDate);

        List<Map<String, Object>> monthlyRevenue = results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("month", result[0]);
            map.put("totalRevenue", result[1]);
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(monthlyRevenue);
    }
	
	@GetMapping("/products/monthly-best")
	public List<Object[]> getMonthlyTopSellingProducts() {
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusMonths(1); // 한 달 전
        Pageable pageable = PageRequest.of(0, 3); // 페이지 사이즈를 10으로 설정
        return orderProductRepository.findTopSellingProductsBetween(startDate, endDate, pageable);
    }
	
	@GetMapping("/products/best")
	public List<Object[]> getTopSellingProducts() {
        Pageable pageable = PageRequest.of(0, 3); // 페이지 사이즈를 10으로 설정
        return orderProductRepository.findTopSellingProducts(pageable);
    }
}
