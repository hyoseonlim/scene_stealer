package pack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
