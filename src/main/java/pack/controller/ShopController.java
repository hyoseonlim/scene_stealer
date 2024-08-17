package pack.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.ProductDto;
import pack.entity.Product;
import pack.model.ShopModel;
import pack.repository.ProductsRepository;

//@Controller
@RestController
public class ShopController {
	@Autowired
	private ShopModel smodel;
	
	@Autowired
	private ProductsRepository productsRepository;
	
	//----Rest 요청
	@GetMapping("/list")
	public ArrayList<Product> getList() {
		ArrayList<Product> list = (ArrayList<Product>)smodel.list();
		//model.addAttribute("list",list);
		return list;
	}
	
	//카테고리별 나열
	 @GetMapping("/list/category/{category}")
	   public List<ProductDto> getProductsByCategory(@PathVariable("category") String category) {
	        List<Product> products = productsRepository.findByCategory(category);
	        return products.stream()
	        		.map(Product::toDto)
	        		.toList();
	    }
	 
	// 제품 상세보기 (no별 제품보기)
	    @GetMapping("/list/product/{no}")
	    public ProductDto getProductDetail(@PathVariable("no") Integer no) {
	    	return smodel.list2(no); 
	    }
	    
	   // 리뷰 읽기(product_no에서 연결해서 review 보기)
	    @GetMapping("/list/review/{no}")
		public ProductDto getReviewList(@PathVariable("no") Integer no) {
			return smodel.list2(no);
			
		}
}
