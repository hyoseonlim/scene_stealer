package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.AlertDto;
import pack.dto.CharacterDto;
import pack.dto.CharacterLikeDto;
import pack.entity.Alert;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.repository.AlertsRepository;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;

@Repository
public class MyPageModel {

	@Autowired
	private CharactersRepository crps;

	@Autowired
	private CharacterLikesRepository clrps;

	@Autowired
	private AlertsRepository arps;

	public List<CharacterDto> myScrapPage(int no) {

		List<CharacterLikeDto> likeList = clrps.findByUserNo(no).stream().map(CharacterLike::toDto)
				.collect(Collectors.toList());
		List<CharacterDto> likeCharacterList = new ArrayList<CharacterDto>();
		for (CharacterLikeDto cl : likeList) {
			crps.findById(cl.getCharacterNo()).map(Character::toDto).ifPresent(likeCharacterList::add);
		}
		return likeCharacterList;
	}

	public List<AlertDto> myAlert(int userNo) {
		return arps.findByUserNo(userNo).stream().map(Alert::toDto).collect(Collectors.toList());
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

}
