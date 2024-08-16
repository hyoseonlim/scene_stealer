package pack.admin.controller;

// 필요한 클래스들을 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import pack.dto.ProductDto;
import pack.admin.model.AdminProductModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @RestController는 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
// 이 클래스는 JSON 형식의 응답을 반환합니다.
@RestController
@RequestMapping("/admin/product")  // /admin/product 경로로 들어오는 요청을 이 컨트롤러에서 처리합니다.
public class AdminProductController {

    @Autowired  // Spring이 AdminProductModel 객체를 자동으로 주입해줍니다.
    private AdminProductModel adminProductModel;

    // GET 요청을 처리하여 모든 상품의 목록을 반환합니다.
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return adminProductModel.list();  // 상품 목록을 반환합니다.
    }

    // GET 요청을 처리하여 특정 상품 번호(no)에 해당하는 상품을 조회합니다.
    @GetMapping("/{no}")
    public ProductDto getProductByNo(@PathVariable("no") Integer no) {
        return adminProductModel.getData(no);  // 해당 번호의 상품 데이터를 반환합니다.
    }

    // POST 요청을 처리하여 새로운 상품을 추가합니다.
    @PostMapping
    public Map<String, Object> addProduct(
            @RequestPart("productDto") String productDtoJson,  // JSON 형태의 상품 데이터를 문자열로 받습니다.
            @RequestPart(value = "pic", required = false) MultipartFile pic) {  // 파일 업로드는 필수가 아닙니다.
        
        Map<String, Object> response = new HashMap<>();  // 응답으로 보낼 데이터를 담을 Map 객체를 생성합니다.
        try {
            // JSON 데이터를 ProductDto 객체로 변환합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto dto = objectMapper.readValue(productDtoJson, ProductDto.class);

            // 파일이 존재하는 경우, 파일을 저장합니다.
            if (pic != null && !pic.isEmpty()) {
                String filePath = "path/to/save/" + pic.getOriginalFilename();  // 저장할 파일의 경로를 설정합니다.
                File dest = new File(filePath);  // 파일 객체를 생성합니다.
                pic.transferTo(dest);  // 업로드된 파일을 지정된 경로에 저장합니다.
            }

            // 상품 등록 로직을 실행하고 결과 메시지를 반환받습니다.
            String result = adminProductModel.insert(dto);
            if ("상품 추가 성공".equals(result)) {  // 상품이 성공적으로 추가된 경우
                response.put("isSuccess", true);  // 성공 상태를 응답에 담습니다.
                response.put("message", result);  // 성공 메시지를 응답에 담습니다.
            } else {
                response.put("isSuccess", false);  // 실패 상태를 응답에 담습니다.
                response.put("message", result);  // 실패 메시지를 응답에 담습니다.
            }
        } catch (Exception e) {  // 예외가 발생한 경우
            e.printStackTrace();  // 예외의 스택 트레이스를 출력합니다.
            response.put("isSuccess", false);  // 실패 상태를 응답에 담습니다.
            response.put("message", "서버 오류 발생: " + e.getMessage());  // 오류 메시지를 응답에 담습니다.
        }
        return response;  // 응답을 반환합니다.
    }

    // PUT 요청을 처리하여 특정 상품 번호(no)의 상품을 업데이트합니다.
    @PutMapping("/{no}")
    public Map<String, Object> updateProduct(
            @PathVariable("no") Integer no,  // URL 경로에서 상품 번호를 추출합니다.
            @RequestBody ProductDto dto) {  // JSON 형식의 상품 데이터를 객체로 받아옵니다.
        
        Map<String, Object> response = new HashMap<>();  // 응답으로 보낼 데이터를 담을 Map 객체를 생성합니다.
        String result = adminProductModel.updateProduct(no, dto);  // 상품 업데이트 로직을 실행합니다.
        
        if ("상품 업데이트 성공".equals(result)) {  // 상품이 성공적으로 업데이트된 경우
            response.put("isSuccess", true);  // 성공 상태를 응답에 담습니다.
            response.put("message", result);  // 성공 메시지를 응답에 담습니다.
        } else {
            response.put("isSuccess", false);  // 실패 상태를 응답에 담습니다.
            response.put("message", result);  // 실패 메시지를 응답에 담습니다.
        }
        return response;  // 응답을 반환합니다.
    }

    // DELETE 요청을 처리하여 특정 상품 번호(no)의 상품을 삭제합니다.
    @DeleteMapping("/{no}")
    public Map<String, Object> deleteProduct(
            @PathVariable("no") Integer no) {  // URL 경로에서 상품 번호를 추출합니다.
        
        Map<String, Object> response = new HashMap<>();  // 응답으로 보낼 데이터를 담을 Map 객체를 생성합니다.
        String result = adminProductModel.deleteProduct(no);  // 상품 삭제 로직을 실행합니다.
        
        if ("상품 삭제 성공".equals(result)) {  // 상품이 성공적으로 삭제된 경우
            response.put("isSuccess", true);  // 성공 상태를 응답에 담습니다.
            response.put("message", result);  // 성공 메시지를 응답에 담습니다.
        } else {
            response.put("isSuccess", false);  // 실패 상태를 응답에 담습니다.
            response.put("message", result);  // 실패 메시지를 응답에 담습니다.
        }
        return response;  // 응답을 반환합니다.
    }
}
