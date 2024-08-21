package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.model.SearchModel;
import pack.dto.ShowDto;

@RestController
@CrossOrigin("*")
public class SearchController {
    
    @Autowired
    private SearchModel model;
    
    // 자동완성
    @GetMapping("/user/search/{category}")
    public List<?> autocomplete(
        @PathVariable("category") String category, 
        @RequestParam("term") String term) {
        return model.autocomplete(category, term);
    }
    
    /**
     * 카테고리와 검색어에 따른 검색 결과를 반환하는 메서드
     */
    @GetMapping("/user/search/{category}/{term}")
    public Map<String, Object> search(
        @PathVariable("category") String category, 
        @PathVariable("term") String term) {
        
        Map<String, Object> result = new HashMap<>();
        List<?> searchResults;

        try {
            switch (category.toLowerCase()) {
                case "actor":
                    System.out.println("Searching shows for actor with name: " + term);
                    searchResults = model.findShowsByActorName(term); // 배우 이름으로 쇼 검색
                    break;
                case "show":
                    System.out.println("Searching shows with term: " + term);
                    searchResults = model.searchShows(term); // 쇼 제목으로 검색
                    break;
                case "product":
                    System.out.println("Searching products with term: " + term);
                    searchResults = model.searchProducts(term); // 상품 이름으로 검색
                    break;
                default:
                    throw new IllegalArgumentException("Unknown category: " + category);
            }

            result.put("results", searchResults);
            System.out.println("Search results: " + searchResults);
        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }

        return result;
    }
}