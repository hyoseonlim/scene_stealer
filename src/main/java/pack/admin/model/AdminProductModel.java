package pack.admin.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pack.dto.ProductDto;
import pack.dto.ReviewDto;
import pack.entity.Product;
import pack.entity.Review;
import pack.repository.ProductsRepository;
import pack.repository.ReviewsRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class AdminProductModel {

    @Autowired
    private ProductsRepository productReposi;
    @Autowired
    private ReviewsRepository repositoryl;

//    // 페이징된 상품 리스트 조회 메서드
//    public Page<ProductDto> listAll(Pageable pageable) {
//        Page<Product> products = productReposi.findAll(pageable);
//        return products.map(product -> {
//            ProductDto dto = Product.toDto(product);
//            dto.setReviewCount(repositoryl.countByProductNo(product.getNo())); // 리뷰 갯수 설정
//            return dto;
//        });
//    }
// // 페이징된 상품 리스트 조회 메서드
    public Page<ProductDto> listAll(Pageable pageable) {
        Page<Product> products = productReposi.findAllByOrderByNoDesc(pageable);
        return products.map(product -> {
            ProductDto dto = Product.toDto(product);
            int reviewCount = repositoryl.countByProduct(product.getNo());  // 리뷰 갯수 조회
            dto.setReviewCount(reviewCount);  // 리뷰 갯수 설정 (null이 아닌 0 이상 값이 설정되도록 보장)

            // findAverageRatingByProduct 메서드가 반환하는 값이 null일 경우 BigDecimal.ZERO를 사용하고, 
            // 그렇지 않으면 반환된 값을 BigDecimal로 캐스팅하여 사용
            BigDecimal averageRating = repositoryl.findAverageRatingByProduct(product.getNo()) == null 
                    ? BigDecimal.ZERO
                    : repositoryl.findAverageRatingByProduct(product.getNo());
            dto.setScore(averageRating);
            return dto;
        });
    }
 // 검색 기능을 추가한 메서드
    public Page<ProductDto> searchProducts(Pageable pageable, String searchTerm, String searchField, String startDate, String endDate) {
        Page<Product> products;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        switch (searchField) {
            case "name":
                products = productReposi.findByNameContainingIgnoreCaseOrderByNoDesc(searchTerm, pageable);
                break;
            case "date":
                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
                products = productReposi.findByDateBetween(start, end, pageable);
                break;
            case "category":
                products = productReposi.findByCategoryContainingIgnoreCaseOrderByNoDesc(searchTerm, pageable);
                break;
            default:
                products = productReposi.findAllByOrderByNoDesc(pageable);
                break;
        }

        return products.map(product -> {
            ProductDto dto = Product.toDto(product);
            int reviewCount = repositoryl.countByProduct(product.getNo());  // 리뷰 갯수 조회
            dto.setReviewCount(reviewCount);  // 리뷰 갯수 설정
            BigDecimal averageRating = repositoryl.findAverageRatingByProduct(product.getNo()) == null 
                    ? BigDecimal.ZERO
                    : repositoryl.findAverageRatingByProduct(product.getNo());
            dto.setScore(averageRating);
            return dto;
        });
    }
//
//    // 검색 기능을 추가한 메서드
//    public Page<ProductDto> searchProducts(Pageable pageable, String searchTerm, String searchField, String startDate, String endDate) {
//        Page<Product> products;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        switch (searchField) {
//            case "name":
//                products = productReposi.findByNameContainingIgnoreCase(searchTerm, pageable);
//                break;
//            case "date":
//                LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
//                LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
//                products = productReposi.findByDateBetween(start, end, pageable);
//                break;
//            case "category":
//                products = productReposi.findByCategoryContainingIgnoreCase(searchTerm, pageable);
//                break;
//            default:
//                products = productReposi.findAll(pageable);
//                break;
//        }
//        return products.map(Product::toDto);
//    }

//    // 특정 no에 해당하는 상품 조회
//    public ProductDto getData(Integer no) {
//        Product product = productReposi.findByNo(no);
//        return product != null ? Product.toDto(product) : null;
//    }
 // 특정 no에 해당하는 상품 조회
    public ProductDto getData(Integer no) {
        Product product = productReposi.findByNo(no);
        if (product != null) {
            ProductDto dto = Product.toDto(product);
            int reviewCount = repositoryl.countByProduct(no);  // 리뷰 갯수 조회
            dto.setReviewCount(reviewCount);  // 리뷰 갯수 설정
            return dto;
        }
        return null;
    }

    // 상품 추가
    public String insert(ProductDto dto) {
        // 이름 중복 확인
        if (productReposi.existsByName(dto.getName())) {
            return "이미 등록된 상품 이름입니다.";
        }
        productReposi.save(ProductDto.toEntity(dto));
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
            upProduct.setPic(dto.getPic() == null ? upProduct.getPic() : dto.getPic());
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
    // 특정 상품의 리뷰를 페이징된 형태로 조회하는 메서드
    public Page<ReviewDto> getProductReviews(int productId, Pageable pageable) {
        Page<Review> reviews = repositoryl.findByProduct(productId, pageable);
        return reviews.map(Review::toDto);
    }
}
