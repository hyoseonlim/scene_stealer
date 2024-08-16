package pack.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.CharacterDto;
import pack.dto.ItemDto;
import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.dto.StyleDto;
import pack.dto.SubDto;
import pack.entity.Character;
import pack.entity.CharacterLike;
import pack.entity.Item;
import pack.entity.Post;
import pack.entity.Review;
import pack.entity.Show;
import pack.entity.Style;
import pack.repository.CharacterLikesRepository;
import pack.repository.CharactersRepository;
import pack.repository.ItemsRepository;
import pack.repository.PostsRepository;
import pack.repository.ReviewsRepository;
import pack.repository.ShowsRepository;
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
	    
	    List<CharacterDto> clist = new ArrayList<>();
	    List<StyleDto> slist = new ArrayList<>();
	    List<ItemDto> ilist = new ArrayList<>(); 

	    for (Integer c : dto.getCharacterNo()) {
	        crps.findById(c).map(Character::toDto)
	        .ifPresent(cdto -> {
	            clist.add(cdto);
	            for (Integer s : cdto.getStyleNo()) {
	                strps.findById(s).map(Style::toDto).ifPresent(sdto -> {
	                	slist.add(sdto);
	                	for(Integer i : sdto.getItemNoList()) {
	                		irps.findById(i).map(Item::toDto).ifPresent(ilist::add);
	                	};
	                });
	            }
	        });
	    }

	    return SubDto.builder()
	                 .show(dto)
	                 .characters(clist)
	                 .styles(slist)
	                 .items(ilist)
	                 .build();
	}

	public boolean isLike(int no, String id) {
		return clrps.findByCharacterNoAndUserId(no, id) == null ? true : false;
	}

}
