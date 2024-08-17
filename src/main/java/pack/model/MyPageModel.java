package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.CharacterDto;
import pack.dto.CharacterLikeDto;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;

@Repository
public class MyPageModel {
	
	@Autowired
	private CharactersRepository crps;
	
	@Autowired
	private CharacterLikesRepository clrps;
	
	public List<CharacterDto> myScrapPage(int no) {
		
		List<CharacterLikeDto> likeList = clrps.findByUserNo(no).stream().map(CharacterLike::toDto).collect(Collectors.toList());
		List<CharacterDto> likeCharacterList = new ArrayList<CharacterDto>();
		for (CharacterLikeDto cl : likeList) {
			crps.findById(cl.getCharacterNo()).map(Character::toDto).ifPresent(likeCharacterList::add);
		}
		return likeCharacterList;
	}

}
