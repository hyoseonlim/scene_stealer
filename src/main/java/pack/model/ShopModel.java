package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.CharacterDto;
import pack.dto.ItemDto;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.dto.ShopDto;
import pack.dto.ShowDto;
import pack.dto.StyleDto;
import pack.dto.SubDto;
import pack.entity.Product;
import pack.entity.Review;
import pack.entity.Show;
import pack.repository.ProductsRepository;
import pack.repository.ReviewsRepository;

@Repository
public class ShopModel {
//Dao라고 생각하고 만들어
	@Autowired
	private ProductsRepository productsRepository;
	
	@Autowired
	private ReviewsRepository reviewsRepository;
	

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
	public ShopDto myreviewshow(Integer no) {
		List<ProductDto> dto =  productsRepository.findByNo(no).stream().map(Product::toDto).toList();
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
	
	
}

