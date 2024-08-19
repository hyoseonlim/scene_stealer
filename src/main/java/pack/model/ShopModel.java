package pack.model;

import java.util.ArrayList;
import java.util.List;

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
	
	// 리뷰
//	public List<ReviewDto> ProductReview(Integer no) {
//		return reviewsRepository.findById(no).stream().map(Review::toDto).toList();
//	}
	
	// 리뷰 연결
	public ShopDto reviewshow(Integer no) {
		ProductDto dto =  productsRepository.findById(no).stream().map(Product::toDto).toList().get(0);
		List<ReviewDto> rlist = new ArrayList<>();
		
		 return ShopDto.builder()
				 .product(dto)
				 .reviews(rlist)
                 .build();
	}
	
	
	
	
	
}

