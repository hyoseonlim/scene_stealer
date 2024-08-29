package pack.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.OrderDto;
import pack.dto.OrderProductDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.dto.ShopDto;
import pack.dto.ShowDto;
import pack.dto.UserDto;

import pack.entity.Order;
import pack.entity.OrderProduct;
import pack.entity.Product;
import pack.entity.Review;
import pack.entity.Show;
import pack.entity.User;
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

	// 리뷰 연결
	public ShopDto reviewshow(Integer no) {
		ProductDto dto = productsRepository.findById(no).stream().map(Product::toDto).toList().get(0);
		List<ReviewDto> rlist = new ArrayList<>();

		for (Integer i : dto.getReviewNoList()) {
			ReviewDto rdto = new ReviewDto();
			rdto = Review.toDto(reviewsRepository.findById(i).get());
			rlist.add(rdto);
		}

		return ShopDto.builder().product(dto).reviews(rlist).build();
	}

	// 내가 쓴 리뷰만 모아보기
	public ShopDto mybuyreviews(int userNo) {
		List<ReviewDto> rlist = reviewsRepository.findByUserNo(userNo).stream().map(Review::toDto)
				.collect(Collectors.toList());

		// 리뷰에 해당하는 제품 리스트 조회
		List<ProductDto> plist = new ArrayList<>();
		for (ReviewDto review : rlist) {
			ProductDto product = Product.toDto(productsRepository.findById(review.getProductNo()).get());
			if (product != null) {
				plist.add(product);
			}
		}
		return ShopDto.builder().reviews(rlist).mybuyProducts(plist).build();
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
		
		List<Integer> orderNoList = ordersRepository.findByUserNo(userNo).stream().map(Order::getNo).collect(Collectors.toList());
		Page<Order> orderList = ordersRepository.findByNoIn(orderNoList, pageable);
		List<OrderProductDto> orderProductList = orderProductRepository.findByOrderNoIn(orderNoList).stream().map(OrderProduct::toDto).collect(Collectors.toList());
		List<Integer> productNoList = orderProductList.stream().map((x) -> x.getProductNo()).collect(Collectors.toList());
		List<ProductDto> productList = productsRepository.findByNoIn(productNoList).stream().map(Product::toDto).collect(Collectors.toList()); 
		
		
		return ShopDto.builder()
				.orderList(orderList.stream().map(Order::toDto).collect(Collectors.toList()))
				.productList(productList)
				.totalPages(orderList.getTotalPages())
		        .currentPage(orderList.getNumber())
		        .totalElements(orderList.getTotalElements())
				.build();
		
	}
	
	public ShopDto myorderDetail(Integer orderNo) {
		OrderDto orderInfo = Order.toDto(ordersRepository.findById(orderNo).get());
		List<OrderProductDto> orderProductList = orderProductRepository.findByOrderNo(orderInfo.getNo()).stream().map(OrderProduct::toDto).collect(Collectors.toList());
		List<Integer> productNoList = orderProductRepository.findByOrderNo(orderNo).stream().map((p) -> p.getProduct().getNo()).collect(Collectors.toList());
		List<ProductDto> productList = productsRepository.findByNoIn(productNoList).stream().map(Product::toDto).collect(Collectors.toList()); 
		return ShopDto.builder()
				.orderInfo(orderInfo)
				.orderProductList(orderProductList)
				.productList(productList)
				.build();
	}
	
	// 리뷰 작성 기능
	@Transactional
	public boolean writeReview(ReviewDto reviewDto) {
	    try {
	        Review review = Review.builder()
	            .contents(reviewDto.getContents())
	            .score(reviewDto.getScore())
	            .pic(reviewDto.getPic())
	            .product(productsRepository.findById(reviewDto.getProductNo()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID")))
	            .user(usersRepository.findById(reviewDto.getUserNo()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID")))
	            .build();
	        
	        reviewsRepository.save(review);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	//리뷰 수정 기능 - 하다가 멈춤
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
	            .product(existingReview.getProduct())
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
	
	// 장바구니 담기 // 장바구니에 제품 추가
	 @Transactional
	  public boolean addToCart(int userNo, int productNo, int quantity) {
	       try {
	       User user = usersRepository.findById(userNo)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
	            Product product = productsRepository.findById(productNo)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

	            Optional<Order> optionalOrder = ordersRepository.findByUserAndStatus(user, "CART");

	            Order order;
	            if (optionalOrder.isPresent()) {
	                order = optionalOrder.get();
	            } else {
	                order = Order.builder()
	                    .user(user)
	                    .state("CART")
	                    .date(new Date())
	                    .price(0)
	                    .build();
	                ordersRepository.save(order);
	            }

	            OrderProduct orderProduct = OrderProduct.builder()
	                .order(order)
	                .product(product)
	                .quantity(quantity)
	                .build();

	            orderProductRepository.save(orderProduct);

	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	
	    public List<OrderProduct> getCartItems(int userNo) {
	        return orderProductRepository.findCartItemsByUserNoAndState(userNo, "CART");
	    }
	 
	
}
