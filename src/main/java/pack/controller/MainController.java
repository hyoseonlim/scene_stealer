package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.CharacterLikeDto;
import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.SubDto;
import pack.entity.CharacterLike;
import pack.model.MainModel;
import pack.repository.CharacterLikesRepository;

@RestController
public class MainController {
	
	@Autowired
	CharacterLikesRepository clrps;
	
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
		
	@GetMapping("/main/like/{no}/{userNo}")
	public boolean isLike(@PathVariable("no") int cno, @PathVariable("userNo") int uno) {
		return mmd.isLike(cno, uno);
	}
	
	@DeleteMapping("/main/scrap/{no}/{userNo}")
	public Map<String, Boolean> deleteScrap(@PathVariable("no") int cno, @PathVariable("userNo") int uno) {
		
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mmd.deleteScrap(cno, uno));
		return result;
	}
	
	@PostMapping("/main/scrap")
	public Map<String, Boolean> insertScrap(@RequestBody CharacterLikeDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mmd.insertScrap(dto));
		return result;
	}
}
