// 메인(작품, 배역, 배우, 스타일, 아이템) CRUD
package pack.admin.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pack.admin.model.AdminMainModel;
import pack.admin.scrap.Scrap;
import pack.dto.ActorInfoDto;
import pack.dto.CharacterDto;
import pack.dto.ItemDto;
import pack.dto.ItemDto_a;
import pack.dto.ItemInfoDto;
import pack.dto.NoticeDto;
import pack.dto.ShowDto;
import pack.dto.ShowInfoDto;
import pack.dto.StyleDto;
import pack.dto.StyleItemDto;
import pack.entity.Item;
import pack.repository.ProductsRepository;

@RestController
public class AdminMainController {
	@Autowired
	private Scrap scrap;
	
	@Autowired
	private AdminMainModel dao;
	
	// 작품 자동완성 - 입력값이 없을 때) 등록된 작품 전체
	@GetMapping("/admin/show/autocomplete")
    public List<ShowDto> autocompleteallShow() {
        return dao.searchShows();
    }
	
	// 작품 자동완성 - 입력값이 있을 때) 등록된 작품 중 일부
    @GetMapping("/admin/show/autocomplete/{term}")
    public List<ShowDto> autocompleteShow(@PathVariable("term") String term) {
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
	
	// 작품 직접 추가
	@PostMapping("/admin/show/diy") // 스타일 PK
	public int insertShowDIY(@RequestParam("title") String title, @RequestPart("file") MultipartFile pic) {
		ShowDto dto = new ShowDto();
		dto.setTitle(title);
		String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
		Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
		try {
			pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
			dto.setPic("/images/" + pic.getOriginalFilename());
		}catch (Exception e) {
			System.out.println("작품 사진 업로드 실패 " + e);
		}
		return dao.insertShow(dto);
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
    	characterDto.setLikesCount(0);
	    return dao.insertCharacter(characterDto); // 추가된 해당 배역의 PK 반환
	}
	
	// 배우 & 배역 직접 추가
	@PostMapping("/admin/show/{no}/character/diy")
	public ActorInfoDto insertActorAndCharacterDIY(@PathVariable("no") int no, @RequestParam("actorName") String actor, 
			@RequestParam("characterName") String character, @RequestPart("file") MultipartFile pic) {
			// 배우
			int actor_no = dao.checkActor(actor); // 존재여부 확인 (있으면 해당 배우 PK를 바로 받고, 없으면 0)
			if(actor_no == 0) actor_no = dao.insertActor(actor); // 없으면 배우 추가 후 PK 받기
			
			// 작품-배우
			dao.insertShowActor(no, actor_no);
			
			// 배역
			CharacterDto characterDto = new CharacterDto();
			characterDto.setShowNo(no);
			characterDto.setActorNo(actor_no);
			characterDto.setName(character + " 역");
			
			String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
			Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
			try {
				pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
				characterDto.setPic("/images/" + pic.getOriginalFilename());
			}catch (Exception e) {
				System.out.println("캐릭터 사진 업로드 실패 " + e);
			}
	    	characterDto.setLikesCount(0);
		    return dao.insertCharacterDIY(characterDto); // 추가된 해당 배역의 PK 반환
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
	public StyleDto addStyle(@PathVariable("no") int no, @RequestPart("file") MultipartFile pic) {
	    String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
	    Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
	    try {
	        pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
	        StyleDto styleDto = new StyleDto();
	        styleDto.setPic("/images/" + pic.getOriginalFilename());
	        styleDto.setCharacterNo(no);
	        styleDto.setNo(dao.insertStyle(styleDto)); // 스타일 저장 후 PK 받기
	        return styleDto;
	    } catch (Exception e) {
	        System.out.println("에러: " + e);
	        return null;
	    }
	}
	
	// 배역의 전체 아이템 목록 조회
	@GetMapping("/admin/fashion/character/{no}/item") // 캐릭터 PK
	public ArrayList<ItemDto_a> getItemsInfo(@PathVariable("no") int no) {
		return dao.searchItems(no);
	}
	
	// 아이템 자동완성 - 입력값이 없을 때) 등록된 아이템 전체
	@GetMapping("/admin/item/autocomplete")
	public List<ItemDto_a> autocompleteallItem() {
	     return dao.searchItems();
	}
		
	// 아이템 자동완성 - 입력값이 있을 때) 등록된 아이템 중 일부
	@GetMapping("/admin/item/autocomplete/{term}")
	public List<ItemDto_a> autocompleteItem(@PathVariable("term") String term) {
		return dao.searchItems(term);
	}
	
	// 스타일에 기존 아이템 추가
	@PostMapping("/admin/fashion/{style}/item/{item}")
	public void addStyleItem(@PathVariable("style") int style, @PathVariable("item") int item) {
		StyleItemDto styleItemDto = new StyleItemDto();
        styleItemDto.setItemNo(item);
        styleItemDto.setStyleNo(style);
        dao.insertStyleItem(styleItemDto);
	}
	
	@Autowired
	private ProductsRepository productsRepository;
	
	// 아이템 추가
	@PostMapping("/admin/fashion/{no}/item") // 스타일 PK
	public void addItem(@PathVariable("no") int no, @RequestParam("name") String name, @RequestParam("product") int product, @RequestPart("file") MultipartFile pic) {
		String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
	    Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
	    try {
	        pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
	        Item item = new Item();
	        StyleItemDto styleItemDto = new StyleItemDto();
	        
	        item.setName(name);
	        item.setProduct(productsRepository.getReferenceById(product)); // 어거지입니다
	        item.setPic("/images/" + pic.getOriginalFilename());
	        
	        styleItemDto.setItemNo(dao.insertItem(item)); // Item 추가 후 받은 PK로
	        styleItemDto.setStyleNo(no);
	        dao.insertStyleItem(styleItemDto);
	    } catch (Exception e) {
	        System.out.println("에러: " + e);
	    }
	}
	
	// 작품 삭제
	@DeleteMapping("/admin/show/{no}")
	public void deleteShow(@PathVariable("no") int no) {
		dao.deleteShowInfo(no);
	}
	
	// 배역 삭제
	@DeleteMapping("/admin/character/{no}")
	public void deleteCharacter(@PathVariable("no") int no) {
		dao.deleteCharacterInfo(no);
	}
	
	// 스타일 삭제
	@DeleteMapping("/admin/style/{no}")
	public void deleteStyle(@PathVariable("no") int no) {
		dao.deleteStyleInfo(no);
	}
	
	// 특정 스타일에서 아이템 연결 삭제
	@DeleteMapping("/admin/item/{styleNo}/{itemNo}")
	public void deleteStyleItem(@PathVariable("styleNo") int style, @PathVariable("itemNo") int item) {
		dao.deleteStyleItem(style, item);
	}
	
	// 전체 아이템 목록
	@GetMapping("/admin/item")
	public ResponseEntity<Page<ItemInfoDto>> getAllNotices(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "no"));
		Page<ItemInfoDto> items = dao.getItemInfos(pageable);		
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/admin/item/{no}")
	public void deleteItem(@PathVariable("no") int no) {
		dao.deleteItem(no);
	}
	
}
