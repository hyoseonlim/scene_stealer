// 메인(작품, 배역, 배우, 스타일, 아이템) CRUD
package pack.admin.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pack.admin.model.AdminMainModel;
import pack.admin.scrap.Scrap;
import pack.dto.ActorInfoDto;
import pack.dto.CharacterDto;
import pack.dto.ItemDto_a;
import pack.dto.ShowDto;
import pack.dto.ShowInfoDto;
import pack.dto.StyleDto;

@RestController
public class AdminMainController {
	@Autowired
	private Scrap scrap;
	
	@Autowired
	private AdminMainModel dao;
	
	// 자동완성 - 입력값이 없을 때) 등록된 작품 전체
	@GetMapping("/admin/show/autocomplete")
    public List<ShowDto> autocompleteall() {
        return dao.searchShows();
    }
	
	// 자동완성 - 입력값이 있을 때) 등록된 작품 중 일부
    @GetMapping("/admin/show/autocomplete/{term}")
    public List<ShowDto> autocomplete(@PathVariable("term") String term) {
        return dao.searchShows(term);
    }

	// 웹스크래핑) 검색한 작품 정보 조회
	@GetMapping("/admin/scrap/show/{keyword}")
	public ShowDto ScrapShow(@PathVariable("keyword") String keyword) {
		return scrap.scrapShow(keyword);
	}
	
	// 웹스크래핑)  검색한 작품의 배우, 배역 정보 조회
	@GetMapping("/admin/scrap/actors/{keyword}")
	public ArrayList<ActorInfoDto> ScrapActors(@PathVariable("keyword") String keyword) {
		return scrap.scrapActors(keyword);
	}
	
	// 작품 추가
	@PostMapping("/admin/show")
	public int insertShow(@RequestBody ShowDto showdto) {
	    return dao.insertShow(showdto); // 작품 추가 후 PK 반환
	}
	
	// 배우 & 배역 추가
	@PostMapping("/admin/show/{no}/character")
	public int insertActorAndCharacter(@RequestBody ActorInfoDto dto, @PathVariable("no") int no) {
		// 배우
		int actor_no = dao.checkActor(dto.getActor()); // 존재여부 확인 (있으면 해당 배우 PK를 바로 받고, 없으면 0)
		if(actor_no == 0) actor_no = dao.insertActor(dto.getActor()); // 없으면 배우 추가 후 PK 받기
		
		// 작품-배우
		dao.insertShowActor(no, actor_no);
		
		// 배역
		CharacterDto characterDto = new CharacterDto();
    	characterDto.setShowNo(no);
    	characterDto.setActorNo(actor_no);
    	characterDto.setName(dto.getCharacter());
    	characterDto.setPic(dto.getPic());
	    return dao.insertCharacter(characterDto); // 추가된 해당 배역의 PK 반환
	}
	
	// 작품의 배우, 배역 목록 조회
	@GetMapping("/admin/fashion/show/{no}")
	public ShowInfoDto getShowInfo(@PathVariable("no") int no) { // 작품 PK
		ShowInfoDto showInfo = new ShowInfoDto(); // 작품 정보와 배우, 배역 목록 정보를 담는 객체
		showInfo.setShow(dao.searchShow(no));
		showInfo.setActorsInfo(dao.searchActors(no));
		return showInfo;
	}

	// 배역의 스타일 목록 조회
	@GetMapping("/admin/fashion/character/{no}/style") // 캐릭터 PK
	public List<StyleDto> getStylesInfo(@PathVariable("no") int no) {
		return dao.searchStyles(no);
	}
	
	// 배역의 스타일 추가
	@PostMapping("/admin/fashion/character/{no}/style") // 캐릭터 PK
	public int addStyle(@PathVariable("no") int no, @RequestPart("file") MultipartFile pic) {
	    String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
	    Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
	    try {
	        pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
	        StyleDto styleDto = new StyleDto();
	        styleDto.setPic("/images/" + pic.getOriginalFilename());
	        styleDto.setCharacterNo(no);
	        dao.insertStyle(styleDto);
	        return no;
	    } catch (Exception e) {
	        System.out.println("에러: " + e);
	        return 0;
	    }
	}

	
	// 배역의 전체 아이템 목록 조회
	@GetMapping("/admin/fashion/character/{no}/item") // 캐릭터 PK
	public ArrayList<ItemDto_a> getItemsInfo(@PathVariable("no") int no) {
		return dao.searchItems(no);
	}
	
}
