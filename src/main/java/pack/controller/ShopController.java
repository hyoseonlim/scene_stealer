package pack.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.RequestMapping;
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
import pack.dto.StyleItemDto;
import pack.dto.SubDto;
import pack.dto.UserDto;
import pack.entity.Item;
import pack.entity.OrderProduct;
import pack.entity.Product;
import pack.entity.User;
import pack.model.PostsModel;
import pack.model.ShopModel;
import pack.repository.OrderProductRepository;
import pack.repository.ProductsRepository;
import pack.repository.UsersRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ShopController {
	@Autowired
	private ShopModel smodel;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private PostsModel pmodel;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

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
		Page<ProductDto> productDtoPage = productsRepository.findByCategoryOrderByNoDesc(category, pageable)
				.map(Product::toDto);
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

	// 리뷰 디테일 보기
	@GetMapping("/mypage/review/detail/{reviewNo}")
	public ReviewDto getReviewDetail(@PathVariable("reviewNo") int reviewNo) {
		return smodel.getReviewDetail(reviewNo);
	}

	// 리뷰 글쓰기 // putmapping - update
//	   
	// 리뷰 작성 API (이미지 포함)
	@PostMapping("/list/review/{orderProductNo}")
	public ResponseEntity<String> writeReviewWithImage(@PathVariable("orderProductNo") int orderProductNo,
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
		reviewDto.setOrderProductNo(orderProductNo);

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
			@RequestPart("reviewDto") String reviewDtoJson,
			@RequestPart(value = "pic", required = false) MultipartFile pic) {
		try {
			// JSON 문자열을 ReviewDto 객체로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			ReviewDto reviewDto = objectMapper.readValue(reviewDtoJson, ReviewDto.class);

			// 리뷰 업데이트 로직
			boolean success = smodel.updateReview(reviewNo, reviewDto);

			// 파일 업로드 처리
			if (pic != null && !pic.isEmpty()) {
				String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
				Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());

				pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
				reviewDto.setPic("/images/" + pic.getOriginalFilename()); // 파일 경로를 리뷰 DTO에 설정
			}

			// 파일 업로드 후 다시 리뷰 업데이트
			smodel.updateReview(reviewNo, reviewDto);

			// 성공 여부에 따른 응답 처리
			if (success) {
				return ResponseEntity.ok("리뷰 수정이 완료되었습니다.");
			} else {
				return ResponseEntity.status(500).body("리뷰 수정에 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("오류 발생: " + e.getMessage());
		}
	}

	// 리뷰 삭제
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

//	@PostMapping("/cart/stock")
//	public List<Map<String, Object>> stockCheck(@RequestBody List<Integer> productNos) {
//		return smodel.stockCheck(productNos);
//	}

	// 재고량 체크 API
	@PostMapping("/cart/stock")
	public List<Map<String, Object>> stockCheck(@RequestBody Map<String, Object> requestData) {
		// 요청 데이터에서 productNos와 userNo를 추출
		List<Object> productNosRaw = (List<Object>) requestData.get("productNos");
		List<Integer> productNos = new ArrayList<>();

		// productNos를 String에서 Integer로 변환
		for (Object productNoRaw : productNosRaw) {
			try {
				productNos.add(Integer.parseInt(productNoRaw.toString())); // String을 Integer로 변환
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid productNo format: " + productNoRaw);
			}
		}

		Integer userNo;
		try {
			userNo = Integer.parseInt(requestData.get("userNo").toString()); // String을 Integer로 변환
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid userNo format");
		}

		// 재고 확인 로직
		List<Map<String, Object>> result = new ArrayList<>();

		for (Integer productNo : productNos) {
			Product product = productsRepository.findById(productNo).orElse(null);
			if (product != null) {
				Map<String, Object> productStock = new HashMap<>();
				productStock.put("productNo", productNo);
				productStock.put("stock", product.getStock()); // 상품의 재고량을 반환
				productStock.put("available", product.isAvailable()); // 판매 종료된 상품
				result.add(productStock);
			}
		}

		return result; // 재고 정보를 클라이언트로 반환
	}

	@PostMapping("/order")
	public ResponseEntity<Map<String, Boolean>> newOrder(@RequestBody OrderProductAllDto dto) {
		Map<String, Boolean> result = new HashMap<>();

		try {
			boolean isOrderSuccessful = smodel.newOrder(dto);
			result.put("success", isOrderSuccessful);
			return ResponseEntity.ok(result); // 성공 시 200 OK 응답과 함께 결과 반환
		} catch (Exception e) {
			result.put("success", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result); // 실패 시 500 오류 응답
		}
	}

	// 주문 취소 API
	@DeleteMapping("/cancel/{orderNo}")
	public String cancelOrder(@PathVariable("orderNo") Integer orderNo) {
		boolean isCancelled = smodel.cancelOrder(orderNo);
		if (isCancelled) {
			return "주문이 성공적으로 취소되었습니다.";
		} else {
			return "주문을 취소할 수 없습니다.";
		}
	}

	// 리뷰 써는지 체크
	@GetMapping("/review/check/{userNo}/{productNo}")
	public ResponseEntity<Boolean> checkReview(@PathVariable("userNo") int userNo,
			@PathVariable("productNo") int productNo) {
		boolean hasReviewed = smodel.userReviewed(userNo, productNo);
		return ResponseEntity.ok(hasReviewed);
	}

}
