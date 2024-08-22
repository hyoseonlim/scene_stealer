package pack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.model.SearchModel;
import pack.dto.UserDto;

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
        @PathVariable("category") String category, // URL 경로에서 카테고리를 받습니다.
        @PathVariable("term") String term) { // URL 경로에서 검색어를 받습니다.
        
        // 검색 결과를 저장할 Map 객체를 생성합니다.
        Map<String, Object> result = new HashMap<>();
        List<?> searchResults;

        try {
            // 카테고리에 따라 검색 로직을 분기합니다.
            switch (category.toLowerCase()) {
                case "actor":
                    // 배우 이름으로 쇼를 검색합니다.
                    searchResults = model.findShowsByActorName(term); 
                    break;
                case "show":
                    // 쇼 제목으로 쇼를 검색합니다.
                    searchResults = model.searchShows(term); 
                    break;
                case "product":
                    // 상품 이름으로 상품을 검색합니다.
                    searchResults = model.searchProducts(term); 
                    break;
                case "user":
                    // 사용자 ID와 닉네임으로 사용자 검색을 수행합니다.
                    List<UserDto> usersById = model.searchUsersId(term);
                    List<UserDto> usersByNickname = model.searchUsersNickname(term);

                    // 중복을 제거하기 위해 Set을 사용합니다.
                    Set<UserDto> uniqueUsers = new HashSet<>(usersByNickname);
                    uniqueUsers.addAll(usersById);

                    // Set을 List로 변환하여 검색 결과를 설정합니다.
                    searchResults = new ArrayList<>(uniqueUsers);
                    break;
                default:
                    // 유효하지 않은 카테고리인 경우 예외를 발생시킵니다.
                    throw new IllegalArgumentException("Unknown category: " + category);
            }

            // 검색 결과를 Map에 추가합니다.
            result.put("results", searchResults);
        } catch (IllegalArgumentException e) {
            // 예외가 발생한 경우 오류 메시지를 Map에 추가합니다.
            result.put("error", e.getMessage());
        }

        // 결과를 반환합니다.
        return result;
    }
}