package pack.model;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import pack.dto.FollowDto;
import pack.dto.NoticeDto;
import pack.entity.Alert;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.Notice;
import pack.entity.User;
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

	public Page<CharacterDto> myScrapPage(int no, Pageable pageable) {

		List<Integer> characterNoList = clrps.findByUserNo(no).stream().map((res) -> res.getCharacter().getNo())
				.collect(Collectors.toList());
		Page<Character> characterPage = crps.findByNoIn(characterNoList, pageable);
		List<CharacterDto> characterDtoList = characterPage.stream().map(Character::toDto).collect(Collectors.toList());
		return new PageImpl<>(characterDtoList, pageable, characterPage.getTotalElements());
	}

	public Page<AlertDto> myAlert(int userNo, Pageable pageable) {
		Page<Alert> alertPage = arps.findByUserNoOrderByNoDesc(userNo, pageable);
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

	@Transactional
	public boolean updateAlert(int alertNo) {
		try {
			Alert alert = arps.findById(alertNo).get();
			alert.setIsRead(true);
			arps.save(alert);
			return true;
		} catch (Exception e) {
			System.out.println("updateAlert ERROR : " + e.getMessage());
			return false;
		}
	}

	@Transactional
	public boolean insertAlert(String category, String value, int userNo, AlertDto dto) {
		try {
			String path = dto.getPath();
			dto.setCategory("커뮤니티");
			String userNickname;

			if (category.equals("follow")) {
				userNickname = urps.findById(userNo).get().getNickname();
				dto.setContent(userNickname + "님이 나를 팔로우하기 시작했습니다.");
				dto.setPath("/user/style/" + dto.getUserNo() + "/followList/follower");
			} else {
				String postTitledecoded = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
				userNickname = urps.findById(userNo).get().getNickname();
				dto.setPath("/user/style/detail/" + path);

				if (category.equals("reply")) {
					if (value.equals("recomment")) {
						dto.setContent("내 댓글에 " + userNickname + "님이 답댓글을 작성했습니다.");
					} else {
						dto.setContent("내 포스트에 " + userNickname + "님이 댓글을 작성했습니다.");
					}
				} else if (category.equals("like")) {
					dto.setContent(userNickname + " 님이 " + postTitledecoded + "를 좋아합니다.");
				}
			}

			arps.save(AlertDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertAlert ERROR : " + e.getMessage());
			return false;
		}
	}
	
	public Page<CouponDto> getCouponData(int userNo, Pageable pageable) {
		List<Integer> couponNoList = cpurps.findByUserNoAndIsUsedIsNull(userNo).stream().map((cu) -> cu.getCoupon().getNo()).collect(Collectors.toList());
		Page<Coupon> couponPage = cprps.findByNoIn(couponNoList, pageable);
		List<CouponDto> couponDtoList = couponPage.stream().map(Coupon::toDto).collect(Collectors.toList());
		return new PageImpl<>(couponDtoList, pageable, couponPage.getTotalElements());
	}
	
	public boolean nicknameCheck(String nick) {
		boolean b = false;
		Optional<User> user = urps.findByNickname(nick);
		if (user.isPresent()) {
			b = true;
		}
		return b;
	}
}
