package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.AlertDto;
import pack.dto.CharacterDto;
import pack.model.MyPageModel;

@RestController
public class MyPageController {

	@Autowired
	private MyPageModel mm;
	
	@GetMapping("/myScrapPage/{no}")
	public List<CharacterDto> myScrapPage(@PathVariable("no") int no) {
		return mm.myScrapPage(no);
	}
	
	@GetMapping("/alert/{no}")
	public List<AlertDto> myAlert(@PathVariable("no") int userNo) {
		return mm.myAlert(userNo);
	}
	 
	@DeleteMapping("/alert/{alertNo}")
	public Map<String, Boolean> deleteAlert(@PathVariable("alertNo") int alertNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", mm.deleteAlert(alertNo));
		return result;
	}
	
}
