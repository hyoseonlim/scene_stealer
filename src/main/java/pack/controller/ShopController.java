package pack.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.ProductDto;
import pack.entity.Product;
import pack.model.ShopModel;
import pack.repository.ProductsRepository;

@Controller
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
	 @GetMapping("/list/{category}")
	   public List<ProductDto> getProductsByCategory(@PathVariable("category") String category) {
	        List<Product> products = productsRepository.findByCategory(category);
	        return products.stream()
	        		.map(Product::toDto)
	        		.toList();
	    }
}
