		package pack.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.entity.Product;
import pack.repository.ProductsRepository;
import pack.admin.model.AdminProductModel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private AdminProductModel adminProductModel;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "searchField", defaultValue = "name") String searchField,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "category", defaultValue = "") String category, // 카테고리 추가
            Pageable pageable) {

        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<ProductDto> productPage;

        if (searchTerm.isEmpty() && (startDate == null || endDate == null) && category.isEmpty()) {
            // 조건이 없을 경우 전체 목록
            productPage = adminProductModel.listAll(pageable);
        } else {
            // 검색 필터 사용
            productPage = adminProductModel.searchProducts(pageable, searchTerm, searchField, startDate, endDate, category);
        }

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{no}")
    public ProductDto getProductByNo(@PathVariable("no") Integer no) {
        return adminProductModel.getData(no);
    }

    @PostMapping
    public Map<String, Object> addProduct(
            @RequestPart("productDto") String productDtoJson,
            @RequestPart(value = "pic", required = false) MultipartFile pic) {

        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto dto = objectMapper.readValue(productDtoJson, ProductDto.class);

            if (pic != null && !pic.isEmpty()) {
                String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                Path imagePath = Paths.get(staticDirectory, pic.getOriginalFilename());
                File dest = imagePath.toFile();

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                pic.transferTo(dest);
                dto.setPic("/images/" + pic.getOriginalFilename());
            }
            dto.setCount(0);
            dto.setAvailable(true);
            String result = adminProductModel.insert(dto);
            response.put("isSuccess", "상품 추가 성공".equals(result));
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("isSuccess", false);
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/{no}")
    public Map<String, Object> updateProduct(
            @PathVariable("no") Integer no,
            @RequestPart("productDto") String productDtoJson,
            @RequestPart(value = "pic", required = false) MultipartFile pic) {

        Map<String, Object> response = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto dto = objectMapper.readValue(productDtoJson, ProductDto.class);

            if (pic != null && !pic.isEmpty()) {
                String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                Path imagePath = Paths.get(staticDirectory, pic.getOriginalFilename());
                File dest = imagePath.toFile();

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                pic.transferTo(dest);
                dto.setPic("/images/" + pic.getOriginalFilename());
            }

            String result = adminProductModel.updateProduct(no, dto);
            response.put("isSuccess", "상품 업데이트 성공".equals(result));
            response.put("message", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("isSuccess", false);
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        return response;
    }
    
    @Autowired
    ProductsRepository productRepo;
    
    // 자동완성 - 입력값이 없을 때) 등록된 상품 전체
 	@GetMapping("/autocomplete")
 	public List<ProductDto> autocompleteallProduct() {
 	     return productRepo.findAll().stream().map(Product::toDto).toList();
 	}
 		
 	// 자동완성 - 입력값이 있을 때) 등록된 상품 중 일부
 	@GetMapping("/autocomplete/{term}")
 	public List<ProductDto> autocompleteProduct(@PathVariable("term") String term) {
 		return productRepo.findByNameContaining(term).stream().map(Product::toDto).toList();
 	}
 	
 	// 특정 상품의 리뷰를 페이징된 형태로 조회
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Page<ReviewDto>> getProductReviews(
            @PathVariable("productId") int productId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviewsPage = adminProductModel.getProductReviews(productId, pageable);

        return ResponseEntity.ok(reviewsPage);
    }
    
    // 품절 처리
    @PutMapping("/soldout/{no}")
    public void toSoldOut(@PathVariable("no") Integer no) {
    	productRepo.updateStockToZeroByNo(no);
    }
    
    @DeleteMapping("/{no}")
    public void toFinishSelling(@PathVariable("no") Integer no) {
    	productRepo.setProductUnavailable(no);
    }
    
}
