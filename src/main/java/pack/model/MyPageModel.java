package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageImpl;

import jakarta.transaction.Transactional;
import pack.dto.AlertDto;
import pack.dto.CharacterDto;
import pack.dto.CharacterLikeDto;
import pack.dto.CouponDto;
import pack.dto.CouponUserDto;
import pack.dto.NoticeDto;
import pack.entity.Alert;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.Notice;
import pack.repository.AlertsRepository;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;
import pack.repository.CouponUserRepository;
import pack.repository.CouponsRepository;
import pack.repository.UsersRepository;

@Repository
public class MyPageModel {

	@Autowired
	private CharactersRepository crps;

	@Autowired
	private CharacterLikesRepository clrps;

	@Autowired
	private AlertsRepository arps;

	@Autowired
	private CouponsRepository cprps;

	@Autowired
	private CouponUserRepository cpurps;

	@Autowired
	private UsersRepository urps;

	public List<CharacterDto> myScrapPage(int no) {

		List<CharacterLikeDto> likeList = clrps.findByUserNo(no).stream().map(CharacterLike::toDto)
				.collect(Collectors.toList());
		List<CharacterDto> likeCharacterList = new ArrayList<CharacterDto>();
		for (CharacterLikeDto cl : likeList) {
			crps.findById(cl.getCharacterNo()).map(Character::toDto).ifPresent(likeCharacterList::add);
		}
		return likeCharacterList;
	}

	public Page<AlertDto> myAlert(int userNo, Pageable pageable) {
		Page<Alert> alertPage = arps.findByUserNo(userNo, pageable);
		return alertPage.map(Alert::toDto);
	}

	@Transactional
	public boolean deleteAlert(int alertNo) {
		boolean b = false;
		try {
			if (arps.deleteByNo(alertNo) > 0) {
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deleteAlert ERROR : " + e.getMessage());
		}
		return b;
	}

	public Page<CouponDto> getCouponData(int userNo, Pageable pageable) {
		List<Integer> couponNoList = cpurps.findByUserNo(userNo).stream().map(CouponUser::getNo)
				.collect(Collectors.toList());
		Page<Coupon> couponPage = cprps.findByNoIn(couponNoList, pageable);
		List<CouponDto> couponDtoList = couponPage.stream().map(Coupon::toDto).collect(Collectors.toList());
		return new PageImpl<>(couponDtoList, pageable, couponPage.getTotalElements());
	}
}
