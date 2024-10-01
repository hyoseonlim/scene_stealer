package pack.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.AlertDto;
import pack.dto.CharacterDto;
import pack.dto.CouponDto;
import pack.model.MyPageModel;

@RestController
@RequestMapping("/api")
public class MyPageController {

	@Autowired
	private MyPageModel mm;

	@GetMapping("/myScrapPage/{no}")
	public ResponseEntity<Page<CharacterDto>> myScrapPage(@PathVariable("no") int no, Pageable pageable) {
		Page<CharacterDto> scrapPage = mm.myScrapPage(no, pageable);
		return ResponseEntity.ok(scrapPage);
	}



	@GetMapping("/alert/{no}")
	public ResponseEntity<Page<AlertDto>> myAlert(@PathVariable("no") int userNo,
			@RequestParam(value = "category", defaultValue = "전체") String category, Pageable pageable) {
		Page<AlertDto> alertPage = mm.myAlert(userNo, category, pageable);
		return ResponseEntity.ok(alertPage);
	}

	@DeleteMapping("/alert/{alertNo}")
	public Map<String, Boolean> deleteAlert(@PathVariable("alertNo") int alertNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.deleteAlert(alertNo));
		return result;
	}

	@PutMapping("/alert/{alertNo}")
	public Map<String, Boolean> updateAlert(@PathVariable("alertNo") int alertNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.updateAlert(alertNo));
		return result;
	}

	@PostMapping("/alert/{category}/{value}/{userNo}")
	public Map<String, Boolean> insertAlert(@PathVariable("category") String category,
			@PathVariable("value") String value, @PathVariable("userNo") int userNo, @RequestBody AlertDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.insertAlert(category, value, userNo, dto));

		return result;
	}

	@GetMapping("/coupon/{userNo}")
	public ResponseEntity<Map<String, Object>> getCouponData(@PathVariable("userNo") int userNo, Pageable pageable) {

		Page<CouponDto> couponPage = mm.getCouponData(userNo, pageable);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("content", couponPage.getContent());
		response.put("totalPages", couponPage.getTotalPages());
		response.put("currentPage", couponPage.getNumber());
		response.put("totalElements", couponPage.getTotalElements());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/nickname/check/{nickname}")
	public Map<String, Boolean> nicknameCheck(@PathVariable("nickname") String nn) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.nicknameCheck(nn));
		return result;
	}

	@GetMapping("/alert/Readcheck/{userNo}")
	public Map<String, Boolean> readCheck(@PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.readCheck(userNo));
		return result;
	}

}
