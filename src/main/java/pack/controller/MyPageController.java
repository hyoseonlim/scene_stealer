package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.AlertDto;
import pack.dto.CharacterDto;
import pack.dto.CouponDto;
import pack.dto.NoticeDto;
import pack.model.MyPageModel;

@RestController
public class MyPageController {

	@Autowired
	private MyPageModel mm;

	@GetMapping("/myScrapPage/{no}")
	public ResponseEntity<Page<CharacterDto>> myScrapPage(@PathVariable("no") int no, Pageable pageable) {
		Page<CharacterDto> scrapPage = mm.myScrapPage(no, pageable);
	    return ResponseEntity.ok(scrapPage);
	}

	@GetMapping("/alert/{no}")
	public ResponseEntity<Page<AlertDto>> myAlert(@PathVariable("no") int userNo, Pageable pageable) {
		Page<AlertDto> alertPage = mm.myAlert(userNo, pageable);
		return ResponseEntity.ok(alertPage);
	}

	@DeleteMapping("/alert/{alertNo}")
	public Map<String, Boolean> deleteAlert(@PathVariable("alertNo") int alertNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.deleteAlert(alertNo));
		return result;
	}

	@GetMapping("/coupon/{userNo}")
	public ResponseEntity<Map<String, Object>> getCouponData(@PathVariable("userNo") int userNo, Pageable pageable) {
 
		Page<CouponDto> noticePage = mm.getCouponData(userNo, pageable);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("content", noticePage.getContent());
		response.put("totalPages", noticePage.getTotalPages());
		response.put("currentPage", noticePage.getNumber());
		response.put("totalElements", noticePage.getTotalElements());
		return ResponseEntity.ok(response);
	}

}
