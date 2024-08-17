// 메인(작품, 배역, 배우, 스타일, 아이템) CRUD
package pack.admin.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.scrap.Scrap;
import pack.dto.ScrapDto;

@RestController
public class AdminMainController {
	
	@Autowired
	private Scrap scrap;
	
	@GetMapping("/admin/scrap/{keyword}")
	public ArrayList<ScrapDto> getOneNotie(@PathVariable("keyword") String keyword) {
		return scrap.scrapActors(keyword);
	}
}
