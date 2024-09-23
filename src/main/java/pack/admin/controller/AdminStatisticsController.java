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

import pack.dto.ProductReturnRateDto;
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
	public ResponseEntity<List<Map<String, Object>>> getMonthlyTopSellingProducts() {
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusMonths(1); // 한 달 전
        Pageable pageable = PageRequest.of(0, 3); // 페이지 사이즈를 10으로 설정
        List<Object[]> results = orderProductRepository.findTopSellingProductsBetween(startDate, endDate, pageable);
        
        List<Map<String, Object>> bestsellers = results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("no", result[0]);
            map.put("name", result[1]);
            map.put("quantity", result[2]);
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(bestsellers);
    }
	
	@GetMapping("/products/best")
	public ResponseEntity<List<Map<String, Object>>> getTopSellingProducts() {
        Pageable pageable = PageRequest.of(0, 5); // 페이지 사이즈를 10으로 설정       
        List<Object[]> results = orderProductRepository.findTopSellingProducts(pageable);
        
        List<Map<String, Object>> bestsellers = results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("no", result[0]);
            map.put("name", result[1]);
            map.put("quantity", result[2]);
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(bestsellers);
    }
	
	@GetMapping("/products/return-rate")
	public List<ProductReturnRateDto> getSadProducts() {
        Pageable pageable = PageRequest.of(0, 3); // 페이지 사이즈를 10으로 설정       
        List<Object[]> results = orderProductRepository.findProductReturnRates(pageable);
        return results.stream()
            .map(result -> {
                Integer productNo = (Integer) result[0];
                String productName = (String) result[1];
                Integer deliveredQuantity = ((Number) result[2]).intValue();
                Integer canceledQuantity = ((Number) result[3]).intValue();
                double returnRate = (double) canceledQuantity / (deliveredQuantity + canceledQuantity);
                return new ProductReturnRateDto(productNo, productName, deliveredQuantity, canceledQuantity, returnRate);
            })
            .sorted((p1, p2) -> Double.compare(p2.getReturnRate(), p1.getReturnRate())) // 내림차순 정렬
            .collect(Collectors.toList());
    }
}
