package pack.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.entity.Product;
import pack.repository.ProductsRepository;

@Repository
public class ShopModel {
//Dao라고 생각하고 만들어
	@Autowired
	private ProductsRepository productsRepository;
	
	public List<Product> list(){//전체 자료 읽기
		List<Product> list = productsRepository.findAll();
		
		return list;
	}}
