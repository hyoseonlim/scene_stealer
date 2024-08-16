package pack.admin.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pack.dto.ProductDto;
import pack.entity.Product;
import pack.repository.ProductsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AdminProductModel {

    @Autowired
    private ProductsRepository productReposi;

    // 상품 리스트 조회
    public List<ProductDto> list() {
        return productReposi.findAll().stream().map(Product::toDto).collect(Collectors.toList());
    }

    // 특정 no에 해당하는 상품 조회
    public ProductDto getData(Integer no) {
        Product product = productReposi.findByNo(no);
        return product != null ? Product.toDto(product) : null;
    }

    // 상품 추가
    public String insert(ProductDto dto) {
        // 이름 중복 확인
        if (productReposi.existsByName(dto.getName())) {
            return "이미 등록된 상품 이름입니다.";
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setContents(dto.getContents());
        product.setDate(dto.getDate());
        product.setCategory(dto.getCategory());
        product.setPic(dto.getPic());
        product.setStock(dto.getStock());
        product.setDiscountRate(dto.getDiscountRate());
        product.setScore(dto.getScore());
        productReposi.save(product);
        return "상품 추가 성공";
    }

    // 상품 업데이트
    public String updateProduct(Integer no, ProductDto dto) {
        Optional<Product> updateprodcut = productReposi.findById(no);
        if (updateprodcut.isPresent()) {
            Product upProduct = updateprodcut.get();

            // 상품 정보 업데이트
            upProduct.setName(dto.getName());
            upProduct.setPrice(dto.getPrice());
            upProduct.setContents(dto.getContents());
            upProduct.setDate(dto.getDate());
            upProduct.setCategory(dto.getCategory());
            upProduct.setPic(dto.getPic());
            upProduct.setStock(dto.getStock());
            upProduct.setDiscountRate(dto.getDiscountRate());
            upProduct.setScore(dto.getScore());

            // 업데이트된 상품 저장
            productReposi.save(upProduct);
            return "상품 업데이트 성공";
        } else {
            return "상품을 찾을 수 없습니다.";
        }
    }

    // 상품 삭제
    public String deleteProduct(Integer no) {
        Optional<Product> deleteproduct = productReposi.findById(no);
        if (deleteproduct.isPresent()) {
            productReposi.deleteById(no);
            return "상품 삭제 성공";
        } else {
            return "상품을 찾을 수 없습니다.";
        }
    }
}
