package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.CharacterDto;
import pack.dto.CharacterLikeDto;
import pack.dto.ItemDto;
import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.StyleDto;
import pack.dto.StyleItemDto;
import pack.dto.SubDto;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.Item;
import pack.entity.Post;
import pack.entity.Review;
import pack.entity.Show;
import pack.entity.Style;
import pack.entity.StyleItem;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;
import pack.repository.ItemsRepository;
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
	

	public List<ShowDto> mainShowData() {
		return srps.findShowAll().stream().map(Show::toDto).toList();
	}

	public List<ReviewDto> mainShowReview() {
		return rrps.findTop3ByOrderByNoDesc().stream().map(Review::toDto).toList();
	}

	public List<PostDto> mainShowPosts() {
		return prps.findTop3ByOrderByNoDesc().stream().map(Post::toDto).toList();
	}

	public SubDto subShowData(int no) {
	    ShowDto dto = srps.findById(no).stream().map(Show::toDto).toList().get(0);
	    
	    List<CharacterDto> clist = new ArrayList<CharacterDto>();
	    List<StyleDto> slist = new ArrayList<StyleDto>();
	    List<StyleItemDto> silist = new ArrayList<StyleItemDto>();
	    List<ItemDto> ilist = new ArrayList<ItemDto>(); 
	    
	    for (Integer c : dto.getCharacterNo()) {
	        crps.findById(c).map(Character::toDto).ifPresent(cdto -> {
	        	// c : show 의 character 번호
	        	clist.add(cdto);
	            for (Integer s : cdto.getStyleNo()) {
	            	// s : character의 style 번호
	                strps.findById(s).map(Style::toDto).ifPresent(sdto -> {
	                	slist.add(sdto);
	                	
	                });
	            for (Integer si : cdto.getStyleNo()) {
	            	for(StyleItemDto i : sirps.findByStyleNo(si).stream().map(StyleItem::toDto).collect(Collectors.toList())) {
	            		silist.add(i);
                		irps.findById(i.getItemNo()).map(Item::toDto).ifPresent(ilist::add);
                	};
	            }
	            }
	        });
	    }
	    
	    return SubDto.builder()
	                 .show(dto)
	                 .characters(clist)
	                 .styles(slist)
	                 .styleItems(silist)
	                 .items(ilist)
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
			clrps.save(CharacterLikeDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertScrap ERROR : " + e.getMessage());
			return false;
		}
	}

}
