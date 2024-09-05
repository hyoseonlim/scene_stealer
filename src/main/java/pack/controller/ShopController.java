package pack.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import pack.dto.CouponDto;
import pack.dto.OrderDto;
import pack.dto.OrderProductAllDto;
import pack.dto.OrderProductDto;
import pack.dto.PersonalCouponDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.dto.ShopDto;
import pack.dto.SubDto;
import pack.dto.UserDto;
import pack.entity.Product;
import pack.entity.User;
import pack.model.PostsModel;
import pack.model.ShopModel;
import pack.repository.ProductsRepository;
import pack.repository.UsersRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ShopController {
	@Autowired
	private ShopModel smodel;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private PostsModel pmodel;

	@Autowired
	private UsersRepository usersRepository;

	// ----Rest 요청
	@GetMapping("/list")
	public ResponseEntity<Page<ProductDto>> getList(Pageable pageable) {
		Page<ProductDto> list = smodel.list(pageable); // Pageable 객체로 데이터를 가져옴
		return ResponseEntity.ok(list);
	}

	// 카테고리별 나열
	@GetMapping("/list/category/{category}")
	public ResponseEntity<Page<ProductDto>> getProductsByCategory(@PathVariable("category") String category,
			Pageable pageable) {

		// Product 엔티티를 ProductDto로 변환
		Page<ProductDto> productDtoPage = productsRepository.findByCategory(category, pageable).map(Product::toDto);
		// 변환된 Page<ProductDto>를 반환
		return ResponseEntity.ok(productDtoPage);
	}

	// 제품 상세보기 (no별 제품보기)
	@GetMapping("/list/product/{no}")
	public ProductDto getProductDetail(@PathVariable("no") Integer no) {
		return smodel.list2(no);
	}

//	// 상품별 리뷰보기(제품, 리뷰 일 대 다)
//	@GetMapping("/list/review/{no}")
//	public ShopDto reviewData(@PathVariable("no") Integer no) {
//		return smodel.reviewshow(no);
//	}
	// 상품별 리뷰보기 (페이징 처리)
    @GetMapping("/list/review/{no}")
    public ResponseEntity<ShopDto> reviewData(@PathVariable("no") Integer no, Pageable pageable) {
        ShopDto shopDto = smodel.reviewshow(no, pageable); // pageable을 전달하여 페이징 처리
        return ResponseEntity.ok(shopDto);
    }
	// 내가 쓴 리뷰 보기
//	@GetMapping("/mypage/review/{userNo}")
//	public ShopDto myreviewOnly(@PathVariable("userNo") int userNo) {
//		return smodel.mybuyreviews(userNo);
//	}
    @GetMapping("/mypage/review/{userNo}")
    public ResponseEntity<ShopDto> myreviewOnly(@PathVariable("userNo") int userNo, Pageable pageable) {
        ShopDto shopDto = smodel.mybuyreviews(userNo, pageable); // 페이징 정보를 함께 전달
        return ResponseEntity.ok(shopDto);
    }

	// 주문 내역 보기
	@GetMapping("/order/orderlist/{userNo}")
	public ResponseEntity<ShopDto> myorder(@PathVariable("userNo") Integer userNo, Pageable pageable) {

		ShopDto orderListPage = smodel.myorder(userNo, pageable);

		return ResponseEntity.ok(orderListPage);
	}

	// 주문 내역 상세 보기
	@GetMapping("/order/orderdetail/{orderNo}")
	public Map<String, Object> myorderDetail(@PathVariable("orderNo") Integer orderNo,
			@RequestParam("userNo") int userNo) {
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("order", smodel.myorderDetail(orderNo));
		result.put("user", pmodel.userInfo(userNo));

		return result;
	}

	// 결제하기
	@PostMapping("/purchase")
	public ResponseEntity<ShopDto> checkout(@RequestBody OrderDto orderDto) {

		System.out.println(orderDto); // 로그 출력으로 확인
		try {
			// 1. 결제 처리
			boolean paymentSuccess = smodel.processPayment(orderDto);
			if (!paymentSuccess) {
				return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(null); // 결제 실패 시 응답
			}

			// 2. 주문 저장
			smodel.saveOrder(orderDto);

			// 3. 저장된 주문 내역 상세 보기로 리다이렉트
			ShopDto orderDetail = smodel.myorderDetail(orderDto.getNo());
			return ResponseEntity.ok(orderDetail);
		} catch (Exception e) {
			// 예외 처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// 리뷰 디테일 보기
	@GetMapping("/mypage/review/detail/{reviewNo}")
	public ReviewDto getReviewDetail(@PathVariable("reviewNo") int reviewNo) {
		return smodel.getReviewDetail(reviewNo);
	}

	// 리뷰 글쓰기 // putmapping - update
//	   
	// 리뷰 작성 API (이미지 포함)
	@PostMapping("/list/review/{productNo}")
	public ResponseEntity<String> writeReviewWithImage(@PathVariable("productNo") int productNo,
			@RequestPart("reviewDto") String reviewDtoJson, // reviewDto JSON으로 수신
			@RequestPart(value = "pic", required = false) MultipartFile pic) {

		// JSON 데이터를 객체로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		ReviewDto reviewDto;
		try {
			reviewDto = objectMapper.readValue(reviewDtoJson, ReviewDto.class);
		} catch (Exception e) {
			return ResponseEntity.status(400).body("잘못된 요청입니다: " + e.getMessage());
		}

		// reviewDto의 productNo 필드에 @PathVariable에서 받은 값을 설정
		reviewDto.setProductNo(productNo);

		// 이미지가 있는 경우 처리
		if (pic != null && !pic.isEmpty()) {
			try {
				// 이미지 파일을 저장할 경로 설정
				String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
				Path imagePath = Paths.get(staticDirectory, pic.getOriginalFilename());
				File dest = imagePath.toFile();

				// 경로가 존재하지 않으면 폴더 생성
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdirs();
				}

				// 이미지 파일 저장
				pic.transferTo(dest);

				// 이미지 경로를 ReviewDto에 설정
				reviewDto.setPic("/images/" + pic.getOriginalFilename());
			} catch (Exception e) {
				return ResponseEntity.status(500).body("이미지 업로드에 실패했습니다: " + e.getMessage());
			}
		}

		// 리뷰 작성 처리
		boolean success = smodel.writeReview(reviewDto);

		if (success) {
			return ResponseEntity.ok("리뷰 작성이 완료되었습니다.");
		} else {
			return ResponseEntity.status(500).body("리뷰 작성에 실패했습니다.");
		}
	}

	// 리뷰 수정
	@PutMapping("/review/update/{reviewNo}")
	public ResponseEntity<String> updateReview(@PathVariable("reviewNo") int reviewNo,
			@RequestBody ReviewDto reviewDto) {
		boolean success = smodel.updateReview(reviewNo, reviewDto);
		if (success) {
			return ResponseEntity.ok("리뷰 수정이 완료되었습니다.");
		} else {
			return ResponseEntity.status(500).body("리뷰 수정에 실패했습니다.");
		}
	}

//	    // 리뷰 삭제
	@DeleteMapping("/review/delete/{reviewNo}")
	public ResponseEntity<String> deleteReview(@PathVariable("reviewNo") int reviewNo) {
		boolean success = smodel.deleteReview(reviewNo);
		if (success) {
			return ResponseEntity.ok("리뷰 삭제가 완료되었습니다.");
		} else {
			return ResponseEntity.status(500).body("리뷰 삭제에 실패했습니다.");
		}
	}

	// 해당 유저의 쿠폰함
	@GetMapping("/coupon/order/{no}")
	public List<PersonalCouponDto> getCouponListByUser(@PathVariable("no") int no) {
		return smodel.getCouponListByUser(no);
	}

	@PostMapping("/cart/stock")
	public List<Map<String, Object>> stockCheck(@RequestBody List<Integer> productNos) {
		return smodel.stockCheck(productNos);
	}
	
	@PostMapping("/order")
	public ResponseEntity<Map<String, Boolean>> newOrder(@RequestBody OrderProductAllDto dto) {
	    Map<String, Boolean> result = new HashMap<>();
	    
	    try {
	        boolean isOrderSuccessful = smodel.newOrder(dto);
	        result.put("success", isOrderSuccessful);
	        return ResponseEntity.ok(result);  // 성공 시 200 OK 응답과 함께 결과 반환
	    } catch (Exception e) {
	        result.put("success", false);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);  // 실패 시 500 오류 응답
	    }
	}

}
