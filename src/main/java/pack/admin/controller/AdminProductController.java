package pack.admin.controller;

// 필요한 클래스들을 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import pack.dto.ProductDto;
import pack.admin.model.AdminProductModel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  // 이 클래스는 RESTful 웹 서비스의 컨트롤러로 사용됨
@RequestMapping("/admin/product")  // 이 컨트롤러의 모든 메서드는 /admin/product 경로로 접근 가능
public class AdminProductController {

    @Autowired  // Spring이 AdminProductModel 객체를 자동으로 주입
    private AdminProductModel adminProductModel;

    
//    @GetMapping
//    public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
//        Page<ProductDto> productPage = adminProductModel.listAll(pageable);
//        return ResponseEntity.ok(productPage);
//    }
    
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "searchField", defaultValue = "name") String searchField) {

        Pageable pageable = PageRequest.of(page, size); // 페이지 요청 생성

        Page<ProductDto> productPage;

        if (searchTerm.isEmpty()) {
            // 검색어가 없을 경우 전체 목록을 가져옴
            productPage = adminProductModel.listAll(pageable);
        } else {
            // 검색어가 있을 경우, 검색 조건에 따라 상품을 필터링하여 가져옴
            productPage = adminProductModel.searchProducts(pageable, searchTerm, searchField);
        }

        return ResponseEntity.ok(productPage);
    }


    //  특정 상품 번호(no)에 해당하는 상품을 조회
    @GetMapping("/{no}")
    public ProductDto getProductByNo(@PathVariable("no") Integer no) {
        return adminProductModel.getData(no);  // 해당 번호의 상품 데이터를 반환
    }

    //  새로운 상품을 추가
    @PostMapping
    public Map<String, Object> addProduct(
            @RequestPart("productDto") String productDtoJson,  // 클라이언트가 보낸 JSON 형식의 상품 데이터를 문자열로 받음
            @RequestPart(value = "pic", required = false) MultipartFile pic) {  // 파일 업로드를 처리하며 필수는 아님

        Map<String, Object> response = new HashMap<>();  // 응답 데이터를 담을 맵 객체 생성
        try {
            // JSON 데이터를 ProductDto 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto dto = objectMapper.readValue(productDtoJson, ProductDto.class);

            // 파일이 존재하는 경우, 파일을 저장
            if (pic != null && !pic.isEmpty()) {
                // 파일 저장 경로를 src/main/resources/static/images로 설정
                String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                Path imagePath = Paths.get(staticDirectory, pic.getOriginalFilename());
                File dest = imagePath.toFile();
                
                // 디렉토리가 존재하지 않으면 생성
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                
                pic.transferTo(dest);  // 업로드된 파일을 지정된 경로에 저장
                
                // DTO에 이미지 경로를 설정
                dto.setPic("/images/" + pic.getOriginalFilename());  // 서버에서 접근할 수 있는 경로로 설정
            }

            // 상품 등록 로직을 실행하고 결과 메시지를 반환
            String result = adminProductModel.insert(dto);
            if ("상품 추가 성공".equals(result)) {
                response.put("isSuccess", true);  // 성공 상태와 메시지를 응답에 추가
                response.put("message", result);
            } else {
                response.put("isSuccess", false);  // 실패 상태와 메시지를 응답에 추가
                response.put("message", result);
            }
        } catch (Exception e) {  // 예외가 발생한 경우
            e.printStackTrace();
            response.put("isSuccess", false);  // 에러 상태와 메시지를 응답에 추가
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        return response;  // 응답을 반환
    }

    //  특정 상품 번호(no)의 상품을 업데이트
    @PutMapping("/{no}")
    public Map<String, Object> updateProduct(
            @PathVariable("no") Integer no,  // URL 경로에서 상품 번호를 추출
            @RequestPart("productDto") String productDtoJson,  // 클라이언트가 보낸 JSON 형식의 상품 데이터를 문자열로 받음
            @RequestPart(value = "pic", required = false) MultipartFile pic) {  // 파일 업로드를 처리하며 필수는 아님

        Map<String, Object> response = new HashMap<>();  // 응답 데이터를 담을 맵 객체 생성
        try {
            // JSON 데이터를 ProductDto 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto dto = objectMapper.readValue(productDtoJson, ProductDto.class);

            // 파일이 존재하는 경우, 파일을 저장
            if (pic != null && !pic.isEmpty()) {
                // 파일 저장 경로를 src/main/resources/static/images로 설정
                String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                Path imagePath = Paths.get(staticDirectory, pic.getOriginalFilename());
                File dest = imagePath.toFile();
                
                // 디렉토리가 존재하지 않으면 생성
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                
                pic.transferTo(dest);  // 업로드된 파일을 지정된 경로에 저장

                // DTO에 이미지 경로를 설정
                dto.setPic("/images/" + pic.getOriginalFilename());  // 서버에서 접근할 수 있는 경로로 설정
            }

            // 상품 업데이트 로직을 실행하고 결과 메시지를 반환
            String result = adminProductModel.updateProduct(no, dto);
            if ("상품 업데이트 성공".equals(result)) {
                response.put("isSuccess", true);  // 성공 상태와 메시지를 응답에 추가
                response.put("message", result);
            } else {
                response.put("isSuccess", false);  // 실패 상태와 메시지를 응답에 추가
                response.put("message", result);
            }
        } catch (Exception e) {  // 예외가 발생한 경우
            e.printStackTrace();
            response.put("isSuccess", false);  // 에러 상태와 메시지를 응답에 추가
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        return response;  // 응답을 반환
    }

    // DELETE 요청을 처리하여 특정 상품 번호(no)의 상품을 삭제
    @DeleteMapping("/{no}")
    public Map<String, Object> deleteProduct(@PathVariable("no") Integer no) {
        Map<String, Object> response = new HashMap<>();  // 응답 데이터를 담을 맵 객체 생성
        String result = adminProductModel.deleteProduct(no);  // 상품 삭제 로직을 실행하고 결과 메시지를 반환
        if ("상품 삭제 성공".equals(result)) {
            response.put("isSuccess", true);  // 성공 상태와 메시지를 응답에 추가
            response.put("message", result);
        } else {
            response.put("isSuccess", false);  // 실패 상태와 메시지를 응답에 추가
            response.put("message", result);
        }
        return response;  // 응답을 반환
    }
}
