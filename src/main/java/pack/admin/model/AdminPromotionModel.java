package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.PopupDto;
import pack.dto.ProductDto;
import pack.entity.Product;
import pack.repository.PopupRepository;
import pack.repository.ProductsRepository;

@Repository
public class AdminPromotionModel {
	@Autowired
	private ProductsRepository productRepo;
	
	@Autowired
	private PopupRepository popupRepo;
	
	public List<ProductDto> searchProducts(){
		return productRepo.findAll().stream().map(Product::toDto).toList();
	}
	
	public List<ProductDto> searchProducts(String name){
		return productRepo.findByNameContaining(name).stream().map(Product::toDto).toList();
	}
	
	public void addPopup(PopupDto dto) {
		 popupRepo.save(PopupDto.toEntity(dto)); 
	}
}
