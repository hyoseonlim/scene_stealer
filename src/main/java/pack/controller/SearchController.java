package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    
    // SearchModel 객체를 자동으로 주입합니다.
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
            Pageable pageable = PageRequest.of(page, size);

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
                    searchResults = model.searchUsersId(term, pageable);
                    searchResults = model.searchUsersNickname(term, pageable);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown category: " + category);
            }

            result.put("results", searchResults.getContent());
            result.put("totalPages", searchResults.getTotalPages());
            result.put("currentPage", searchResults.getNumber());
            result.put("totalElements", searchResults.getTotalElements());

        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}