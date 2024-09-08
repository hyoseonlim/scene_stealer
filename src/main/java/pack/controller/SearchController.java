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
    /**
     * 자동완성 기능을 제공하는 메서드입니다.
     * 카테고리와 검색어를 기반으로 자동완성 결과를 반환합니다.
     */
    @GetMapping("/user/search/{category}")
    public List<?> autocomplete(
        @PathVariable("category") String category, // URL 경로에서 카테고리를 받습니다.
        @RequestParam("term") String term) { // 쿼리 파라미터에서 검색어를 받습니다.
        
        // 모델에서 자동완성 결과를 가져옵니다.
        return model.autocomplete(category, term);
    }
    /**
     * 카테고리와 검색어에 따른 검색 결과를 반환하는 메서드입니다.
     * 카테고리와 검색어를 기반으로 다양한 검색 결과를 반환합니다.
     */
    @GetMapping("/user/search/{category}/{term}")
    public Map<String, Object> search(
        @PathVariable("category") String category,
        @PathVariable("term") String term,
        @RequestParam("page") int page, 
        @RequestParam("size") int size) {

        Map<String, Object> result = new HashMap<>();
        Page<?> searchResults;

        try {
            // 페이지 인덱스가 0 이상인지 확인
            if (page < 0) {
                throw new IllegalArgumentException("Page index must not be less than zero");
            }

            Pageable pageable = PageRequest.of(page, size); // 페이지는 0부터 시작하므로 수정하지 않음

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
                    Page<?> searchResultsId = model.searchUsersId(term, pageable);
                    Page<?> searchResultsNickname = model.searchUsersNickname(term, pageable);
                    
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
            result.put("currentPage", searchResults.getNumber() + 1); // 1 기반 페이지로 표시
            result.put("totalElements", searchResults.getTotalElements());

        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}
