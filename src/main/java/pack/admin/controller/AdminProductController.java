package pack.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pack.dto.ProductDto;
import pack.admin.model.AdminProductModel;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

	@Autowired
	private AdminProductModel adminProductModel;

	// 상품 목록 조회
	@GetMapping
	public List<ProductDto> getAllProducts() {
		return adminProductModel.list();
	}

	// 특정 no에 해당하는 상품 조회
	@GetMapping("/{no}")
	public ProductDto getProductByNo(@PathVariable("no") Integer no) {
		return adminProductModel.getData(no);
	}

	// 상품 추가
	@PostMapping
	public ResponseEntity<String> addProduct(@RequestBody ProductDto dto) {
		String result = adminProductModel.insert(dto);
		if ("상품 추가 성공".equals(result)) {
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
	}

	// 상품 업데이트
	@PutMapping("/{no}")
	public ResponseEntity<String> updateProduct(@PathVariable("no") Integer no, @RequestBody ProductDto dto) {
		String result = adminProductModel.updateProduct(no, dto);
		if ("상품 업데이트 성공".equals(result)) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}

	// 상품 삭제
	@DeleteMapping("/{no}")
	public ResponseEntity<String> deleteProduct(@PathVariable("no") Integer no) {
		String result = adminProductModel.deleteProduct(no);
		if ("상품 삭제 성공".equals(result)) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}
}
