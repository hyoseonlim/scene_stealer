package pack.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Page<ProductDto>> getList(Pageable pageable) {
        Page<ProductDto> list = smodel.list(pageable);  // Pageable 객체로 데이터를 가져옴
        return ResponseEntity.ok(list);
    }
	
	//카테고리별 나열
    @GetMapping("/list/category/{category}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategory(
        @PathVariable("category") String category, Pageable pageable) {
        
     // Product 엔티티를 ProductDto로 변환
        Page<ProductDto> productDtoPage = productsRepository.findByCategory(category, pageable)
                                                            .map(Product::toDto);
      // 변환된 Page<ProductDto>를 반환
        return ResponseEntity.ok(productDtoPage);
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
	    
	    
	    // 리뷰 글쓰기 // putmapping - update
	    // 상품별 리뷰 insert
	    @PostMapping("/list/review/{no}")
	    public ResponseEntity<String> writeReview(
	            @PathVariable int no, 
	            @RequestBody ReviewDto reviewDto) {

	        // reviewDto의 productNo 필드에 @PathVariable에서 받은 값을 설정
	        reviewDto.setProductNo(no);

	        // 리뷰 작성 처리
	        boolean success = smodel.writeReview(reviewDto);
	        
	        if (success) {
	            return ResponseEntity.ok("리뷰 작성이 완료되었습니다.");
	        } else {
	            return ResponseEntity.status(500).body("리뷰 작성에 실패했습니다.");
	        }
	    }
	    // 리뷰 수정
//	    @PutMapping("/review/update/{reviewNo}")
//	    public ResponseEntity<String> updateReview(@PathVariable("reviewNo") int reviewNo, @RequestBody ReviewDto reviewDto) {
//	        boolean success = smodel.updateReview(reviewNo, reviewDto);
//	        if (success) {
//	            return ResponseEntity.ok("리뷰 수정이 완료되었습니다.");
//	        } else {
//	            return ResponseEntity.status(500).body("리뷰 수정에 실패했습니다.");
//	        }
//	    }
//
//	    // 리뷰 삭제
//	    @DeleteMapping("/review/delete/{reviewNo}")
//	    public ResponseEntity<String> deleteReview(@PathVariable("reviewNo") int reviewNo) {
//	        boolean success = smodel.deleteReview(reviewNo);
//	        if (success) {
//	            return ResponseEntity.ok("리뷰 삭제가 완료되었습니다.");
//	        } else {
//	            return ResponseEntity.status(500).body("리뷰 삭제에 실패했습니다.");
//	        }
//	    }
	    
	    
	    
	    
	    
	    
	    
	    // 장바구니 담기 @PostMapping
	    // 장바구니 조회 @GetMapping
	    // 장바구니 품목 선택 삭제 @DeleteMapping
	    // 장바구니 물건 전체 구매 @PostMapping
	    
	    
}
