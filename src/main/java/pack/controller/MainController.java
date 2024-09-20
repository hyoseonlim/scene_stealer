package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.formatter.qual.ReturnsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import pack.dto.CharacterLikeDto;
import pack.dto.PopupDto;
import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.SubDto;
import pack.entity.Product;
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

	@GetMapping("/main/showDataAll")
	public ResponseEntity<Page<ShowDto>> mainShowDataAll(Pageable pageable) {
		Page<ShowDto> noticePage = mmd.mainShowData(pageable);
		return ResponseEntity.ok(noticePage);
	}

	@GetMapping("/main/showNewReview")
	public List<ReviewDto> mainShowReview() {
		return mmd.mainShowReview();
	}

	@GetMapping("/main/showStyleBest")
	public List<PostDto> mainShowPosts() {
		return mmd.mainShowPosts();
	}
	
	// Item이 1개 이상인 Style 데이터 수
	@GetMapping("/main/forRandom")
	public Integer forRandom() {
		return mmd.forRandom();
	}

	// 랜덤 Style 선택
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

	@GetMapping("/main/popup")
	public List<PopupDto> getMainPopup() {
		return mmd.getMainPopup();
	}

	@GetMapping("/main/popupSet/{userNo}")
	public Map<String, Boolean> popupCookie(HttpServletResponse response, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		boolean b = false;
		try {
			Cookie cookie = new Cookie(userNo + "_popup", "DO_NOT_SHOW_POPUP");
			cookie.setMaxAge(24 * 60 * 60); // 유효기간 하루 (초 단위)
			cookie.setPath("/");
			response.addCookie(cookie);
			b = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		result.put("result", b);
		return result;
	}
	
	@GetMapping("/main/scrap/count/{characterNo}")
	public  Map<String, Integer> scrapCountNow(@PathVariable("characterNo") int characterNo) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("likesCount", mmd.scrapCountNow(characterNo));
		return result;
	}
}
