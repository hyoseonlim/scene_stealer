// 쿠폰 발급 (쿠폰테이블 처리 & 알림 전송) , 광고 (알림 전송)
package pack.admin.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pack.admin.model.AdminMainModel;
import pack.admin.model.AdminPromotionModel;
import pack.dto.AlertDto_a;
import pack.dto.CouponDto;
import pack.dto.CouponUserDto_a;
import pack.dto.PopupDto;
import pack.dto.StyleDto;
import pack.entity.Coupon;
import pack.entity.Popup;
import pack.entity.User;
import pack.repository.AlertsRepository;
import pack.repository.CouponUserRepository;
import pack.repository.CouponsRepository;
import pack.repository.PopupRepository;
import pack.repository.UsersRepository;

@RestController
public class AdminPromotionController {
	
	@Autowired
	private AdminPromotionModel promotionDao;
	
	@Autowired
	private AdminMainModel mainDao;
	
	@Autowired
	private CouponsRepository couponsRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	
	@Autowired
	private CouponUserRepository couponUserRepo;
	
	@Autowired
	private AlertsRepository alertsRepo;
	
	@Autowired
	private PopupRepository popupRepo;
	
	// 쿠폰 추가 (테이블 처리: coupon, coupon_user, alerts)
	@PostMapping("/admin/coupon")
	public Map<String, Object> insertCoupon(@RequestBody CouponDto couponDto) {
		// 1. 쿠폰 테이블 (쿠폰명, 할인율, 유효기간)
		Coupon newCoupon = couponsRepo.save(CouponDto.toEntity(couponDto));
		// 2. 쿠폰-유저 관계 테이블 (추가된 해당 쿠폰 PK, 유저 PK)	
		CouponUserDto_a cuDto = new CouponUserDto_a();
		cuDto.setCouponNo(newCoupon.getNo());
		
		List<Integer> userNoList = usersRepo.findAll().stream().map(User::getNo).collect(Collectors.toList());
		for(int i=0; i<userNoList.size(); i++) { // 전체 유저 수 만큼 반복 (동일 쿠폰을 유저만 바꿔 추가)
			cuDto.setUserNo(userNoList.get(i));
			couponUserRepo.save(CouponUserDto_a.toEntity(cuDto, couponsRepo, usersRepo));
		}
		
		// 3. 알림 테이블 (유저 PK, 카테고리 = '프로모션', content = 쿠폰명 + 할인율 + '발급완료!')
		AlertDto_a alertDto = new AlertDto_a();
		alertDto.setCategory("프로모션");
		alertDto.setContent("(" + couponDto.getDiscountRate() +"% 할인!)" + couponDto.getName() + "쿠폰이 발급되었습니다.");
		
		for(int i=0; i<userNoList.size(); i++) { // 전체 유저 수 만큼 반복 (동일 쿠폰을 유저만 바꿔 추가)
			alertDto.setUserNo(userNoList.get(i));
			//  🤖🤖🔨 경로가 정해지면 여기에 <유저의 쿠폰함>으로 setPath도 해야됑 🤖🤖🔨
			alertsRepo.save(AlertDto_a.toEntity(alertDto, usersRepo));
		}
		return Map.of("isSuccess", true);
	}
	
	// 광고 알림 추가 전 경로 설정을 위한 자동완성
	@GetMapping("/admin/promotion/autocomplete/{locCategory}/{term}")
    public List<?> autocomplete(@PathVariable("locCategory") String locCategory, @PathVariable("term") String term) {
        switch (locCategory) {
		case "product": return term.equals("") ? promotionDao.searchProducts() : promotionDao.searchProducts(term);
		case "show": return term.equals("") ? mainDao.searchShows() : mainDao.searchShows(term);
		// case "character":
		default: return List.of(); // locCategory가 예상한 값이 아닐 경우 빈 리스트를 반환
        }
	}
	
	// 광고 알림 추가 (테이블 처리: alerts)
	@PostMapping("/admin/advertise")
	public Map<String, Object> insertAd(@RequestBody AlertDto_a alertDto) { // content, path 설정됨
		alertDto.setCategory("프로모션");
		List<Integer> userNoList = usersRepo.findAll().stream().map(User::getNo).collect(Collectors.toList());
		for(int i=0; i<userNoList.size(); i++) { // 전체 유저 수 만큼 반복 (동일 쿠폰을 유저만 바꿔 추가)
			alertDto.setUserNo(userNoList.get(i));
			alertsRepo.save(AlertDto_a.toEntity(alertDto, usersRepo));
		}
		return Map.of("isSuccess", true);
	}
	
	// 팝업 등록
	@PostMapping("/admin/popup")
	public void addPopup(@RequestParam("path") String path, @RequestPart("pic") MultipartFile pic) {
		String staticDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
		Path uploadPath = Paths.get(staticDirectory, pic.getOriginalFilename());
		System.out.println("경로는 " + path);
		try {
			pic.transferTo(uploadPath); // 파일을 지정된 경로에 저장
			PopupDto dto = new PopupDto();
			dto.setPic("/images/" + pic.getOriginalFilename());
			dto.setPath(path);
			dto.setIsShow(true);
			promotionDao.addPopup(dto);
		} catch (Exception e) {
			System.out.println("에러: " + e);
		}
	}
	
	// 쿠폰 목록
	@GetMapping("/admin/coupons")
	public Page<CouponDto> getAllCoupons(Pageable pageable) {
		Page<Coupon> couponPage = couponsRepo.findAll(pageable);
		return couponPage.map(Coupon::toDto);
	}
	
	// 팝업 목록
	@GetMapping("/admin/popups")
	public Page<PopupDto> getAllPopups(Pageable pageable) {
		Page<Popup> popupPage = popupRepo.findAll(pageable);
		return popupPage.map(Popup::toDto);
	}
	
	// 팝업 상태 변경
	@PutMapping("/admin/popup/{no}/status")
    public void updatePopupStatus(@PathVariable("no") Integer no,
                                    @RequestBody Map<String, Object> requestBody) {
        Boolean status = (Boolean) requestBody.get("status");
        promotionDao.updatePopupStatus(no, status);
    }
	
	// 팝업 삭제
    @DeleteMapping("/admin/popup/{no}")
    public void deletePopup(@PathVariable("no") int no) {
        popupRepo.deleteById(no);
    }

}
