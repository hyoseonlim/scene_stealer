package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	private OrderProductRepository opRepository;
	

	public List<ProductDto> list(){//전체 자료 읽기
		 List<ProductDto> list = productsRepository.findAll().stream().map(Product::toDto).toList();
		 return list;
	}
	public ProductDto list2(Integer no) { // 제품 no별 상품 읽기
		return Product.toDto(productsRepository.findById(no).get());
	           
	}
	
	
	// 리뷰 연결
	public ShopDto reviewshow(Integer no) {
		ProductDto dto =  productsRepository.findById(no).stream().map(Product::toDto).toList().get(0);
		List<ReviewDto> rlist = new ArrayList<>();
	
		for (Integer i : dto.getReviewNoList()) {
			ReviewDto rdto = new ReviewDto();
			rdto = Review.toDto(reviewsRepository.findById(i).get());
			rlist.add(rdto);
		}
		
		 return ShopDto.builder()
				 .product(dto)
				 .reviews(rlist)
                 .build();
	}
	
	
	// 내가 쓴 리뷰만 모아보기
	public ShopDto mybuyreviews(int userNo) {
		 List<ReviewDto> rlist = reviewsRepository.findByUserNo(userNo).stream()
	                .map(Review::toDto)
	                .collect(Collectors.toList());
		 
		 // 리뷰에 해당하는 제품 리스트 조회
	        List<ProductDto> plist = new ArrayList<>();
	        for (ReviewDto review : rlist) {
	            ProductDto product = Product.toDto(productsRepository.findById(review.getProductNo()).get());
	            if (product != null) {
	            	plist.add(product);
	            }
	        }
		return ShopDto.builder()
				 .reviews(rlist)
				 .mybuyProducts(plist)
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
		//OrderDto order = Order.toDto(ordersRepository.findById(orderId).get());
        //ProductDto product =  Product.toDto(productsRepository.findById(productId).get());
    	Order order = ordersRepository.findById(orderId).get();
    	Product product = productsRepository.findById(productId).get();
    	
        // OrderProduct 엔티티 생성 및 저장
        OrderProduct orderProduct = OrderProduct.builder()
        		  .order(order)
                  .product(product)
                  .build();

       // return opRepository.save(orderProduct);
        OrderProduct savedOrderProduct = opRepository.save(orderProduct);

        // OrderProduct를 OrderProductDto로 변환하여 반환
        return OrderProduct.toDto(savedOrderProduct);
    }
    
    // 리뷰 디테일
    public ReviewDto getReviewDetail(int reviewNo) {
    	return Review.toDto(reviewsRepository.findById(reviewNo).get());
    }

	
	
	
	
	
	
	
	
}

