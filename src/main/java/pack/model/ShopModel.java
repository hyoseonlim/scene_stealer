package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.OrderDto;
import pack.dto.OrderProductAllDto;
import pack.dto.OrderProductDto;
import pack.dto.PersonalCouponDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.dto.ShopDto;
import pack.dto.UserDto;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.Order;
import pack.entity.OrderProduct;
import pack.entity.Product;
import pack.entity.Review;
import pack.entity.User;
import pack.repository.CouponUserRepository;
import pack.repository.CouponsRepository;
import pack.repository.OrderProductRepository;
import pack.repository.OrdersRepository;
import pack.repository.ProductsRepository;
import pack.repository.ReviewsRepository;
import pack.repository.UsersRepository;

@Repository
public class ShopModel {
//Dao라고 생각하고 만들어
	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ReviewsRepository reviewsRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderProductRepository opRepository;

	@Autowired
	private CouponUserRepository couponUserRepo;

	@Autowired
	private CouponsRepository couponRepo;

//	public List<ProductDto> list() {// 전체 자료 읽기
//		List<ProductDto> list = productsRepository.findAll().stream().map(Product::toDto).toList();
//		return list;
//	}

//    // 전체 자료 읽기 (페이징된 상품 리스트 조회)
	public Page<ProductDto> list(Pageable pageable) {
		Page<Product> products = productsRepository.findAll(pageable);
		return products.map(Product::toDto);
	}

	// 제품 no별 상품 읽기
	public ProductDto list2(Integer no) {
		return Product.toDto(productsRepository.findById(no).get());

	}
	public Page<ReviewDto> getPagedReviewsForProduct(Integer productNo, Pageable pageable) {
	    return reviewsRepository.findByProduct(productNo, pageable)
	                            .map(Review::toDto); // Review 엔티티를 ReviewDto로 변환
	}

//
//	// 리뷰 연결
//	public ShopDto reviewshow(Integer no) {
//		ProductDto dto = productsRepository.findById(no).stream().map(Product::toDto).toList().get(0);
//		List<ReviewDto> rlist = new ArrayList<>();
//
//		for (Integer i : dto.getReviewNoList()) {
//			ReviewDto rdto = new ReviewDto();
//			rdto = Review.toDto(reviewsRepository.findById(i).get());
//			rlist.add(rdto);
//		}
//
//		return ShopDto.builder().product(dto).reviews(rlist).build();
//	}
	public ShopDto reviewshow(Integer no, Pageable pageable) {
	    // 상품 정보 가져오기
	    ProductDto productDto = productsRepository.findById(no)
	                                              .map(Product::toDto)
	                                              .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

	    // 리뷰 페이징 처리
	    Page<Review> reviewPage = reviewsRepository.findByProduct(no, pageable);

	    // 리뷰를 DTO로 변환
	    List<ReviewDto> reviewDtoList = reviewPage.getContent().stream()
	                                              .map(Review::toDto)
	                                              .collect(Collectors.toList());

	    // Page<ReviewDto>로 변환
	    Page<ReviewDto> reviewDtoPage = new PageImpl<>(reviewDtoList, pageable, reviewPage.getTotalElements());

	    // ShopDto에 상품 정보와 리뷰 목록을 함께 포함
	    return ShopDto.builder()
	                  .product(productDto)
	                  .reviews(reviewDtoPage) // 페이징된 리뷰 데이터 전달
	                  .build();
	}
//	// 내가 쓴 리뷰만 모아보기
//	public ShopDto mybuyreviews(int userNo,Pageable pageable) {
//		Page<ReviewDto> rlist = reviewsRepository.findByUserNo(userNo).stream().map(Review::toDto)
//				.collect(Collectors.toList());
//
//		// 리뷰에 해당하는 제품 리스트 조회
//		List<ProductDto> plist = new ArrayList<>();
//		for (ReviewDto review : rlist) {
//			ProductDto product = Product.toDto(productsRepository.findById(review.getProductNo()).get());
//			if (product != null) {
//				plist.add(product);
//			}
//		}
//		return ShopDto.builder().reviews(rlist).mybuyProducts(plist).build();
//	}
	 public ShopDto mybuyreviews(int userNo, Pageable pageable) {
		    // 사용자가 작성한 리뷰 목록을 가져옵니다.
		    Page<Review> reviewPage = reviewsRepository.findByUserNo(userNo, pageable);
		    
		    // 리뷰를 ReviewDto로 변환합니다.
		    List<ReviewDto> rlist = reviewPage.getContent().stream()
		                                      .map(Review::toDto)
		                                      .collect(Collectors.toList());

		    // 제품 정보를 조회하여 List<ProductDto> 생성
		    List<ProductDto> plist = rlist.stream()
		                                  .map(review -> productsRepository.findById(review.getProductNo())
		                                                                    .map(Product::toDto)
		                                                                    .orElse(null))
		                                  .filter(product -> product != null)
		                                  .collect(Collectors.toList());

		    // ReviewDto 리스트를 PageImpl로 감싸서 Page<ReviewDto>로 변환
		    Page<ReviewDto> reviewDtoPage = new PageImpl<>(rlist, pageable, reviewPage.getTotalElements());

		    // ShopDto에 페이징된 리뷰 및 제품 목록을 담아 반환합니다.
		    return ShopDto.builder()
		                  .reviews(reviewDtoPage) // 페이징된 리뷰
		                  .mybuyProducts(plist) // 사용자가 구매한 제품 목록
		                  .build();
		}

	// 내가 산 주문 내역 보기
	public OrderProductDto mybuyorder(Integer no) {
		OrderProductDto order = OrderProduct.toDto(opRepository.findById(no).get());
		return order;
	}

	// 주문과 상품을 연결
	public OrderProductDto createOrderProduct(Integer orderId, Integer productId) {
		// Order 및 Product 엔티티 조회
		Order order = ordersRepository.findById(orderId).get();
		Product product = productsRepository.findById(productId).get();

		// OrderProduct 엔티티 생성 및 저장
		OrderProduct orderProduct = OrderProduct.builder().order(order).product(product).build();
		OrderProduct savedOrderProduct = opRepository.save(orderProduct);
		return OrderProduct.toDto(savedOrderProduct);
	}

	// 리뷰 디테일
	public ReviewDto getReviewDetail(int reviewNo) {
		return Review.toDto(reviewsRepository.findById(reviewNo).get());
	}

	// 특정 유저 주문 내역
	public ShopDto myorder(Integer userNo, Pageable pageable) {

		List<Integer> orderNoList = ordersRepository.findByUserNo(userNo).stream().map(Order::getNo)
				.collect(Collectors.toList());
		Page<Order> orderList = ordersRepository.findByNoIn(orderNoList, pageable);
		List<OrderProductDto> orderProductList = orderProductRepository.findByOrderNoIn(orderNoList).stream()
				.map(OrderProduct::toDto).collect(Collectors.toList());
		List<Integer> productNoList = orderProductList.stream().map((x) -> x.getProductNo())
				.collect(Collectors.toList());
		List<ProductDto> productList = productsRepository.findByNoIn(productNoList).stream().map(Product::toDto)
				.collect(Collectors.toList());

		return ShopDto.builder().orderList(orderList.stream().map(Order::toDto).collect(Collectors.toList()))
				.productList(productList).totalPages(orderList.getTotalPages()).currentPage(orderList.getNumber())
				.totalElements(orderList.getTotalElements()).build();

	}

	// 주문내역 상세 보기
	public ShopDto myorderDetail(Integer orderNo) {
		OrderDto orderInfo = Order.toDto(ordersRepository.findById(orderNo).get());
		List<OrderProductDto> orderProductList = orderProductRepository.findByOrderNo(orderInfo.getNo()).stream()
				.map(OrderProduct::toDto).collect(Collectors.toList());
		List<Integer> productNoList = orderProductRepository.findByOrderNo(orderNo).stream()
				.map((p) -> p.getProduct().getNo()).collect(Collectors.toList());
		List<ProductDto> productList = productsRepository.findByNoIn(productNoList).stream().map(Product::toDto)
				.collect(Collectors.toList());
		return ShopDto.builder().orderInfo(orderInfo).orderProductList(orderProductList).productList(productList)
				.build();
	}

	// 주문 저장 메서드
	public void saveOrder(OrderDto orderDto) {
		Order order = orderDto.toEntity(orderDto);// OrderDto를 Order 엔티티로 변환
		ordersRepository.save(order);

		List<OrderProduct> orderProducts = orderDto.getOrderProducts().stream().map(OrderProductDto::toEntity)
				.collect(Collectors.toList());

		orderProducts.forEach(orderProductRepository::save);
	}

	public boolean processPayment(OrderDto orderDto) {
		// 결제 처리 로직을 구현
		// 결제 성공 여부를 반환
		return true; // 예: 결제가 성공했다고 가정
	}

	public UserDto getUserInfo(int userNo) {
		// 사용자 정보 가져오기 로직
		return new UserDto(); // 실제로는 사용자 정보 반환
	}

	// 리뷰 작성 기능
	@Transactional
	public boolean writeReview(ReviewDto reviewDto) {
		try {
			Review review = Review.builder()
				    .contents(reviewDto.getContents())
				    .score(reviewDto.getScore())
				    .pic(reviewDto.getPic())
				    .orderProduct(OrderProductDto.toEntity(reviewDto.getOrderProduct()))
				    .user(usersRepository.findById(reviewDto.getUserNo())
				        .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")))
				    .build();

			reviewsRepository.save(review);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 리뷰 수정 기능 - 하다가 멈춤
	@Transactional
	public boolean updateReview(int reviewNo, ReviewDto reviewDto) {
		try {
			Review existingReview = reviewsRepository.findById(reviewNo)
					.orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

			// 새로운 Review 객체를 생성
			Review updatedReview = Review.builder()
					.contents(reviewDto.getContents())
					.score(reviewDto.getScore())
					.pic(reviewDto.getPic())
					.orderProduct(existingReview.getOrderProduct())
					.user(existingReview.getUser())
					.build();

			// 변경된 리뷰 엔티티를 저장
			reviewsRepository.save(updatedReview);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//
//	// 리뷰 삭제 기능
	@Transactional
	public boolean deleteReview(int reviewNo) {
		try {
			reviewsRepository.deleteById(reviewNo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 유저 PK로 쿠폰 리스트 가져오기

	public List<PersonalCouponDto> getCouponListByUser(int userNo) {
		List<CouponUser> couponUserList = couponUserRepo.findByUserNo(userNo);
		List<PersonalCouponDto> list = new ArrayList<PersonalCouponDto>();
		for (CouponUser couponUser : couponUserList) {
			PersonalCouponDto dto = new PersonalCouponDto();
			dto.setCouponUser(CouponUser.toDto(couponUser));
			dto.setCoupon(Coupon.toDto(couponUser.getCoupon()));
			list.add(dto);
		}
		return list;
	}
	
	// 재고량 체크
	public List<Map<String, Object>> stockCheck(List<Integer> productNos) {
		List<Map<String, Object>> result = new ArrayList<>();

		for (Integer i : productNos) {
			Product product = productsRepository.findById(i).orElse(null);
			if (product != null) {
				Map<String, Object> productStock = new HashMap<>();
				productStock.put("productNo", i);
				productStock.put("stock", Product.toDto(product).getStock());
				result.add(productStock);
			}
		}
		return result;
	}

	@Transactional
	public boolean newOrder(OrderProductAllDto dto) {
		boolean b = false;

		try {
			// 1. Order 테이블에 먼저 주문 정보 저장
			Order order = Order.builder().user(User.builder().no(dto.getUserNo()).build()).state("주문접수")
					.price((int) dto.getTotalAmount()) // 할인 적용된 최종 금액을 저장
					.build();

			order = ordersRepository.save(order);

			// 2. 총 할인 금액 계산 (원래 총 금액 - 할인 적용된 금액)
			double originalTotalAmount = dto.getItems().stream()
					.mapToDouble(item -> item.getResultPrice() * item.getQuantity()).sum();

			double discountAmount = originalTotalAmount - dto.getTotalAmount(); // 할인된 금액

			// 3. 상품별로 할인 적용하여 저장 (할인 비율을 적용)
			double appliedDiscount = 0.0;
			for (int i = 0; i < dto.getItems().size(); i++) {
				OrderProductAllDto.OrderItemDTO item = dto.getItems().get(i);
				int originalPrice = (int) item.getResultPrice(); // 상품당 가격
				int quantity = item.getQuantity(); // 수량
				int totalProductPrice = originalPrice * quantity; // 상품 총액

				int finalPrice; // 할인 적용된 상품별 최종 가격

				// 마지막 상품에 잔여 할인 금액을 적용하여 정확히 맞춤
				if (i == dto.getItems().size() - 1) {
					finalPrice = (int) Math.round(totalProductPrice - (discountAmount - appliedDiscount));
				} else {
					// 상품별 할인액 = 상품별 가격 비율에 따른 할인 적용
					double productDiscount = (totalProductPrice / originalTotalAmount) * discountAmount;
					appliedDiscount += productDiscount;
					finalPrice = (int) Math.round(totalProductPrice - productDiscount);
				}

				// 4. 상품별 주문 정보 저장
				OrderProduct orderProduct = OrderProduct.builder().order(order) // 저장된 Order 객체 참조
						.product(Product.builder().no(item.getProductNo()).build()) // Product 참조
						.quantity(quantity) // 수량 설정
						.price(finalPrice / quantity) // 할인된 상품 가격 적용
						.build();

				orderProduct = orderProductRepository.save(orderProduct); // OrderProduct 저장
				
				Product product = productsRepository.findById(orderProduct.getProduct().getNo()).get();
				product.setStock(product.getStock() - quantity);
				product.setCount(product.getCount() + quantity); // 판매량 증가
				productsRepository.save(product);
			}
			
			

			if (dto.getCouponNo() != null && dto.getCouponNo() != 0) {
				Optional<CouponUser> couponuser = couponUserRepo.findByCouponNoAndUserNo(dto.getCouponNo(), dto.getUserNo());
				if (couponuser.isPresent()) {
					CouponUser cp = couponuser.get();
					cp.setIsUsed(true);
					couponUserRepo.save(cp);
				}
			}

			b = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			b = false;
		}

		return b;
	}
	
	

}
