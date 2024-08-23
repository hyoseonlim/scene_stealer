// 쿠폰 발급 (쿠폰테이블 처리 & 알림 전송) , 광고 (알림 전송)
package pack.admin.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.AlertDto;
import pack.dto.AlertDto_a;
import pack.dto.CouponDto;
import pack.dto.CouponUserDto;
import pack.dto.CouponUserDto_a;
import pack.entity.Coupon;
import pack.entity.User;
import pack.repository.AlertsRepository;
import pack.repository.CouponUserRepository;
import pack.repository.CouponsRepository;
import pack.repository.UsersRepository;

@RestController
public class AdminPromotionController {
	
	@Autowired
	private CouponsRepository couponsRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	
	@Autowired
	private CouponUserRepository couponUserRepo;
	
	@Autowired
	private AlertsRepository alertsRepo;
	
	// 쿠폰 추가 (총 3개 테이블 처리)
	@PostMapping("/admin/coupon")
	public Map<String, Object> insert(@RequestBody CouponDto couponDto) {
		// 1. 쿠폰 테이블 (쿠폰명, 할인율, 유효기간)
		Coupon newCoupon = couponsRepo.save(CouponDto.toEntity(couponDto));
		// 2. 쿠폰-유저 관계 테이블 (추가된 해당 쿠폰 PK, 유저 PK)	
		CouponUserDto_a cuDto = new CouponUserDto_a();
		cuDto.setCouponNo(newCoupon.getNo());
		
		List<User> userList = usersRepo.findAll();
		List<Integer> userNoList = userList.stream().map(User::getNo).collect(Collectors.toList());
		for(int i=0; i<userNoList.size(); i++) { // 전체 유저 수 만큼 반복 (동일 쿠폰을 유저만 바꿔 추가)
			cuDto.setUserNo(userNoList.get(i));
			couponUserRepo.save(CouponUserDto_a.toEntity(cuDto, couponsRepo, usersRepo));
		}
		
		// 3. 알림 테이블 (유저 PK, 카테고리 = '프로모션', content = 쿠폰명 + 할인율 + '발급완료!'
		AlertDto_a alertDto = new AlertDto_a();
		alertDto.setCategory("프로모션");
		alertDto.setContent("(" + couponDto.getDiscountRate() +"% 할인!)" + couponDto.getName() + "쿠폰이 발급되었습니다.");
		for(int i=0; i<userNoList.size(); i++) { // 전체 유저 수 만큼 반복 (동일 쿠폰을 유저만 바꿔 추가)
			alertDto.setUserNo(userNoList.get(i));
			alertsRepo.save(AlertDto_a.toEntity(alertDto, usersRepo));
		}
		return Map.of("isSuccess", true);
	}

}
