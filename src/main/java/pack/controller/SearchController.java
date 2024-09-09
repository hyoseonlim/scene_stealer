package pack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.model.SearchModel;

@RestController
@CrossOrigin("*")
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
        @RequestParam("page") int page, 
        @RequestParam("size") int size) {

        Map<String, Object> result = new HashMap<>();
        Page<?> searchResults;

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page index must not be less than zero");
            }
        	
            Pageable pageable = PageRequest.of(page, size); // 페이지는 0부터 시작하므로 -1 처리

            switch (category.toLowerCase()) {
                case "actor":
                    searchResults = model.searchActors(term, pageable);
                    break;
                case "show":
                    searchResults = model.searchShows(term, pageable);
                    break;
                case "product":
                    searchResults = model.searchProducts(term, pageable);
                    break;
                case "user":
                    // 두 검색 결과를 병합하려면 별도의 처리 필요 (예: 사용자 ID와 닉네임을 별도로 처리 후 병합)
                    Page<?> searchResultsId = model.searchUsersId(term, pageable);
                    Page<?> searchResultsNickname = model.searchUsersNickname(term, pageable);
                    
                    // 여기서는 단순히 ID와 닉네임 검색 결과를 병합하는 예제
                    // 실제 구현에서는 필요한 데이터 병합 로직을 작성해야 함
                    List<Object> mergedResults = new ArrayList<>();
                    mergedResults.addAll(searchResultsId.getContent());
                    mergedResults.addAll(searchResultsNickname.getContent());

                    searchResults = new PageImpl<>(mergedResults, pageable, 
                            searchResultsId.getTotalElements() + searchResultsNickname.getTotalElements());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown category: " + category);
            }

            result.put("results", searchResults.getContent());
            result.put("totalPages", searchResults.getTotalPages());
            result.put("currentPage", searchResults.getNumber()+1); // 1 기반 페이지로 표시
            result.put("totalElements", searchResults.getTotalElements());

        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}
