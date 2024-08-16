package pack.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.SubDto;
import pack.model.MainModel;

@RestController
public class MainController {
	
	@Autowired
	private MainModel mmd;
		
	@GetMapping("/main/showData")
	public List<ShowDto> mainShowData() {
		return mmd.mainShowData();
	}
	
	@GetMapping("/main/showNewReview")
	public List<ReviewDto> mainShowReview() {
		return mmd.mainShowReview();
	}
	 
	@GetMapping("/main/showStyleBest")
	public List<PostDto> mainShowPosts() {
		return mmd.mainShowPosts();
	}
	
	@GetMapping("/main/sub/{no}")
	public SubDto subData(@PathVariable("no") int no) {
		return mmd.subShowData(no);
	}
	
	@GetMapping("/main/like/{no}/{id}")
	public boolean isLike(@PathVariable("no") int no, @PathVariable("id") String id) {
		return mmd.isLike(no, id);
	}
}
