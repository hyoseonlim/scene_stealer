package pack.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.ProductDto;
import pack.entity.Product;
import pack.repository.ProductsRepository;

@Repository
public class ShopModel {
//Dao라고 생각하고 만들어
	@Autowired
	private ProductsRepository productsRepository;
	
	public List<ProductDto> list(){//전체 자료 읽기
		List<ProductDto> list = productsRepository.findAll().stream().map(Product::toDto).toList();
		
		return list;
	}
	
	public ProductDto list2(Integer no) { // 제품 no별 상품 읽기
		return Product.toDto(productsRepository.findById(no).get());
	           
	}
	
}

