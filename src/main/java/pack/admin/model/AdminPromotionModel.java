package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.ProductDto;
import pack.entity.Product;
import pack.repository.ProductsRepository;

@Repository
public class AdminPromotionModel {
	@Autowired
	private ProductsRepository productRepo;
	
	public List<ProductDto> searchProducts(){
		return productRepo.findAll().stream().map(Product::toDto).toList();
	}
	
	public List<ProductDto> searchProducts(String name){
		return productRepo.findByNameContaining(name).stream().map(Product::toDto).toList();
	}
}
