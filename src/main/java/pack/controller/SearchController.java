package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.entity.Actor;
import pack.entity.Show;
import pack.model.SearchModel;

@RestController
@CrossOrigin("*")
public class SearchController {
	
	@Autowired
	private SearchModel model;
	
	// 자동완성을 위한..
	@GetMapping("/user/search/{category}")
	public List<?> autocomplete(
	    @PathVariable("category") String category, 
	    @RequestParam("term") String term) {
		System.out.println("Category: " + category + ", Term: " + term);
	    return model.search(category, term);
	}
	
    // 카테고리와 검색어에 따른 결과를 반환하는 API
    @GetMapping("/user/search/")
    public Map<String, Object> search(
        @RequestParam("category") String category, 
        @RequestParam("term") String term) {
        
        Map<String, Object> result = new HashMap<>();
        List<?> searchResults = model.search(category, term);
        result.put("results", searchResults);
        
        return result;
    }
    
    // 특정 배우의 쇼를 가져오는 API
    @GetMapping("/user/actor/{actorNo}/shows")
    public Map<String, Object> getShowsByActorNo(@PathVariable("actorNo") int actorNo) {
        List<Show> shows = model.getShowsByActorNo(actorNo);
        Map<String, Object> result = new HashMap<>();
        result.put("shows", shows.stream().map(Show::toDto).toList());
        return result;
    }
	
	
    // 이름으로 배우를 찾는 API 추가
//    @GetMapping("/user/actor/{name}")
//    public Map<String, Object> getActorDataByName(
//        @PathVariable("name") String name) {
//        
//        Map<String, Object> result = new HashMap<>();
//        Optional<Actor> actor = model.findActorByName(name);
//        result.put("data", actor.orElse(null));
//        
//        return result;
//    }
}