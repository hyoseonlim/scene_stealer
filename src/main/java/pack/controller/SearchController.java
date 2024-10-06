package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.model.SearchModel;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class SearchController {
    
    @Autowired
    private SearchModel model;

	// 자동완성
    @GetMapping("/user/search/{category}")
    public List<?> autocomplete(
        @PathVariable("category") String category, // URL 경로에서 카테고리를 받습니다.
        @RequestParam("term") String term) { // 쿼리 파라미터에서 검색어를 받습니다.
        
        // 모델에서 자동완성 결과를 가져옵니다.
        return model.autocomplete(category, term);
    }

    // 검색 결과 반환
    @GetMapping("/user/search/{category}/{term}")
    public Map<String, Object> search(
        @PathVariable("category") String category,
        @PathVariable("term") String term,
        @RequestParam(value = "page", defaultValue = "0") int page, 
        @RequestParam(value = "size", defaultValue = "5") int size) {  // 기본값 5로 설정

        Map<String, Object> result = new HashMap<>();
        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page index must not be less than zero");
            }

            if ("product".equals(category)) {
                size = 9;  // 상품 검색일 경우 9개의 결과를 반환
            }

            result = model.performSearch(category, term, page, size);

        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
        }

        return result;
    }

}
