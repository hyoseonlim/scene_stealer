package pack.controller;

import java.util.List;

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
	
	@GetMapping("/user/{category}")
	public List<?> autocomplete(
	    @PathVariable("category") String category, 
	    @RequestParam("term") String term) {
	    return model.search(category, term);
	}

}