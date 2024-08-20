// 메인(작품, 배역, 배우, 스타일, 아이템) CRUD
package pack.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.scrap.Scrap;
import pack.dto.ActorScrapDto;
import pack.dto.ShowDto;

@RestController
public class AdminMainController {
	
	@Autowired
	private Scrap scrap;
	
	@GetMapping("/admin/scrap/show/{keyword}")
	public ShowDto ScrapShow(@PathVariable("keyword") String keyword) {
		return scrap.scrapShow(keyword);
	}
	
	@GetMapping("/admin/scrap/actors/{keyword}")
	public ArrayList<ActorScrapDto> ScrapActors(@PathVariable("keyword") String keyword) {
		return scrap.scrapActors(keyword);
	}
	
	@PostMapping("/admin/fashion")
	public String insertMainDatas(@RequestBody List<ActorScrapDto> datas) {
		// AdminMainModel에서 작품/배우/배역 CRUD
		return "";
	}
}
