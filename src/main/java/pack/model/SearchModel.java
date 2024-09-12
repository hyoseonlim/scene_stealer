package pack.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import pack.dto.ActorDto;
import pack.dto.CharacterDto;
import pack.dto.ProductDto;
import pack.dto.ShowDto;
import pack.dto.UserDto;
import pack.entity.Actor;
import pack.entity.Character;
import pack.entity.Product;
import pack.entity.Show;
import pack.entity.User;
import pack.repository.ActorsRepository;
import pack.repository.ProductsRepository;
import pack.repository.ShowsRepository;
import pack.repository.UsersRepository;

@Repository
public class SearchModel {

    @Autowired
    private ActorsRepository actorsRepository;

    @Autowired
    private ShowsRepository showsRepository;

    @Autowired 
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    // 자동완성 기능을 처리하는 메서드
    public List<?> autocomplete(String category, String term) {
        switch (category.toLowerCase()) {
            case "actor":
                return autocompleteActors(term);
            case "show":
                return autocompleteShows(term);
            case "product":
                return autocompleteProducts(term);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    // 자동완성용 메서드
    public List<ActorDto> autocompleteActors(String term) {
        return actorsRepository.findByNameContaining(term).stream()
            .map(Actor::toDto)
            .toList();
    }
    
    public List<ShowDto> autocompleteShows(String term) {
        return showsRepository.findByTitleContaining(term).stream()
            .map(Show::toDto)
            .toList();
    }
    
    public List<ProductDto> autocompleteProducts(String term) {
        return productsRepository.findByNameContainingAndAvailableIsTrue(term).stream()
            .map(Product::toDto)
            .toList();
    }

    public Page<ActorDto> searchActors(String term, Pageable pageable) {
        Page<Object[]> actorsPage = actorsRepository.findActorsWithShows(term, pageable);

        return actorsPage.map(objects -> {
            Actor actor = (Actor) objects[0];
            Show show = (Show) objects[1];

            List<String> showDetails = new ArrayList<>();
            if (show != null) {
            	showDetails.add(show.getNo() != null ? show.getNo().toString() : "Unknown No");
                showDetails.add(show.getTitle() != null ? show.getTitle() : "Unknown Title");
                showDetails.add(show.getPic() != null ? show.getPic() : ""); // Handling null picture URL
            } else {
                showDetails.add("Unknown Title");
                showDetails.add(""); // No picture URL
            }

            return new ActorDto(actor.getName(), actor.getNo(), showDetails);
        });
    }

//    public Page<ActorDto> searchActors(String term, Pageable pageable) {
//    	Page<Actor> actorsPage = actorsRepository.findByNameContaining(term, pageable);
//    	return actorsPage.map(Actor::toDto);
//    }
    
    public Page<CharacterDto> searchShows(String term, Pageable pageable) {
        // showsRepository를 사용하여 title을 기반으로 shows를 찾고, 해당 show의 characters를 페이징 처리하여 반환
        Page<Character> charactersPage = showsRepository.findCharactersByShowTitle(term, pageable);
        return charactersPage.map(Character::toDto);
    }

    public Page<ProductDto> searchProducts(String term, Pageable pageable) {
        Page<Product> productsPage = productsRepository.findByNameContainingOrderByNoDesc(term, pageable);
        return productsPage.map(Product::toDto);
    }

    public Page<UserDto> searchUsersNickname(String term, Pageable pageable) {
        Page<User> usersPage = usersRepository.findByNicknameContaining(term, pageable);
        return usersPage.map(User::toDto);
    }
    
    // 사용자 ID로 검색
    public Page<UserDto> searchUsersId(String term, Pageable pageable) {
        Page<User> usersPage = usersRepository.findByIdContaining(term, pageable);
        return usersPage.map(User::toDto);
    }

}