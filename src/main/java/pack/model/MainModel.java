package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.CharacterDto;
import pack.dto.CharacterLikeDto;
import pack.dto.PopupDto;
import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.StyleDto;
import pack.dto.StyleItemDto;
import pack.dto.SubDto;
import pack.entity.Character;
import pack.entity.Item;
import pack.entity.Popup;
import pack.entity.Post;
import pack.entity.Review;
import pack.entity.Show;
import pack.entity.Style;
import pack.entity.StyleItem;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;
import pack.repository.ItemsRepository;
import pack.repository.PopupRepository;
import pack.repository.PostsRepository;
import pack.repository.ReviewsRepository;
import pack.repository.ShowsRepository;
import pack.repository.StyleItemRepository;
import pack.repository.StylesRepository;

@Repository
public class MainModel {

	@Autowired
	private ShowsRepository srps;

	@Autowired
	private ReviewsRepository rrps;

	@Autowired
	private PostsRepository prps;

	@Autowired
	private CharactersRepository crps;

	@Autowired
	private CharacterLikesRepository clrps;

	@Autowired
	private StylesRepository strps;

	@Autowired
	private ItemsRepository irps;
	
	@Autowired
	private StyleItemRepository sirps;
	
	@Autowired
	private PopupRepository purps;
	

	public List<ShowDto> mainShowData() {
		Pageable pageable = PageRequest.of(0, 10);
		return srps.findShowAll(pageable).stream().map(Show::toDto).toList();
	}
	
	public Page<ShowDto> mainShowData(Pageable pageable) {
		
		Page<Show> showPage = srps.findAll(pageable);
		
		return showPage.map(Show::toDto);
	}

	public List<ReviewDto> mainShowReview() {
		return rrps.findTop3ByPicIsNotNullOrderByNoDesc().stream().map(Review::toDto).toList();
	}

	public List<PostDto> mainShowPosts() {
		return prps.findTop5ByDeletedIsFalseOrderByNoDesc().stream().map(Post::toDto).toList();
	}

	public SubDto subShowData(int no) {
	    // PK가 n인 데이터는 삭제되었을 수 있음. 따라서 n번째 데이터를 가져와야 함
		Pageable pageable = PageRequest.of(no-1, 1, Sort.by(Sort.Direction.ASC, "no")); // 0-based index이므로 n-1을 설정하여 원하는 nth 데이터를 가져옴
		Style style = sirps.findAllStyleExceptNoItem(pageable).getContent().get(0);
	    Character character = style.getCharacter();
	    Show show = character.getShow();	    
	    List<String> characters = show.getCharacters().stream().map(c -> c.getName()).toList();
	    
	    List<StyleItem> styleItemList = sirps.findByStyleNo(style.getNo());
	    List<Item> itemList = styleItemList.stream().map(si -> si.getItem()).toList();

	    return SubDto.builder()
	                 .show(Show.toDto(show))
	                 .names(characters)
	                 .character(Character.toDto(character))
	                 .style(Style.toDto(style))
	                 .styleItems(styleItemList.stream().map(StyleItem::toDto).collect(Collectors.toList()))
	                 .items(itemList.stream().map(Item::toDto).collect(Collectors.toList()))
	                 .build();
	    
	}

	public boolean isLike(int uno, int cno) {
		return clrps.findByCharacterNoAndUserNo(uno, cno).size() == 0 ? false : true;
	}
	
	@Transactional
	public boolean deleteScrap(int cno, int uno) {
		boolean b = false;
		try {
			if(clrps.deleteByCharacterNoAndUserNo(cno, uno) > 0) {
				Character c = crps.findById(cno).get();
				c.setLikesCount(c.getLikesCount() - 1);
				crps.save(c);
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deleteScrap ERROR : " + e.getMessage());
		}
		return b;
		
	}
	
	@Transactional
	public boolean insertScrap(CharacterLikeDto dto) {
		try {
			Character c = crps.findById(dto.getCharacterNo()).get();
			c.setLikesCount(c.getLikesCount() + 1);
			crps.save(c);
			clrps.save(CharacterLikeDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertScrap ERROR : " + e.getMessage());
			return false;
		}
	}
	
	public List<PopupDto> getMainPopup() {
		return purps.findByIsShowIsTrue().stream().map(Popup::toDto).collect(Collectors.toList());
	}
	
	public Integer forRandom() {
		return sirps.findAllExceptNoItem();
	}
	
	public Integer scrapCountNow(int characterNo) {
		Optional<Character> crt = crps.findById(characterNo);
		int result = 0;
		if(crt.isPresent()) {
			result = crt.get().getLikesCount();
		}
		return result;
	}

}
