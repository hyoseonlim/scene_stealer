package pack.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.OrderProductDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.dto.ShopDto;
import pack.dto.SubDto;
import pack.dto.UserDto;
import pack.entity.Product;
import pack.model.PostsModel;
import pack.model.ShopModel;
import pack.repository.ProductsRepository;

@RestController
public class ShopController {
	@Autowired
	private ShopModel smodel;
	
	@Autowired
	private ProductsRepository productsRepository;
	
	@Autowired
	private PostsModel pmodel;
	
	//----Rest 요청
	@GetMapping("/list")
	public List<ProductDto> getList() {
		List<ProductDto> list = smodel.list();
		//model.addAttribute("list",list);
		return list;
	}
	
	//카테고리별 나열
	 @GetMapping("/list/category/{category}")
	   public List<ProductDto> getProductsByCategory(@PathVariable("category") String category) {
	        List<Product> products = productsRepository.findByCategory(category);
	        return products.stream()
	        		.map(Product::toDto)
	        		.toList();
	    }
	 
	// 제품 상세보기 (no별 제품보기)
	    @GetMapping("/list/product/{no}")
	    public ProductDto getProductDetail(@PathVariable("no") Integer no) {
	    	return smodel.list2(no); 
	    }
	    
	  // 상품별 리뷰보기(제품, 리뷰 일 대 다)
	    @GetMapping("/list/review/{no}")
		public ShopDto reviewData(@PathVariable("no") Integer no) {
			return smodel.reviewshow(no);
		}  
	    
	    
	    // 내가 쓴 리뷰 보기
	    @GetMapping("/mypage/review/{userNo}")
	    public ShopDto myreviewOnly(@PathVariable("userNo") int userNo) {
			return smodel.mybuyreviews(userNo);
		}
	    
	    // 주문 내역 보기
	    @GetMapping("/order/orderlist/{userNo}")
	    public ResponseEntity<ShopDto> myorder(@PathVariable("userNo") Integer userNo, Pageable pageable) {
	    	
	    	ShopDto orderListPage = smodel.myorder(userNo, pageable);
	    	
	    	return ResponseEntity.ok(orderListPage);
	    }
	    
	    // 주문 내역 상세 보기
	    @GetMapping("/order/orderdetail/{orderNo}")
	    public Map<String, Object> myorderDetail(@PathVariable("orderNo") Integer orderNo, @RequestParam("userNo") int userNo) {
	    	Map<String, Object> result = new HashMap<String, Object>();
	    	
	    	result.put("order", smodel.myorderDetail(orderNo));
	    	result.put("user", pmodel.userInfo(userNo));
	    	
	    	return result;
	    }
	    
	    
	    
	    // 리뷰 디테일 보기
	    @GetMapping("/mypage/review/detail/{reviewNo}")
	    public ReviewDto getReviewDetail(@PathVariable("reviewNo") int reviewNo) {
	    	return smodel.getReviewDetail(reviewNo);
	    }
}
