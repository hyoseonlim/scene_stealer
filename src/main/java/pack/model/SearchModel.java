package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    // 자동완성 기능을 처리하는 메소드
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

    // 자동완성용 메소드
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
    
    // 검색을 수행하는 메소드
    public Map<String, Object> performSearch(String category, String term, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        Page<?> searchResults;
        Pageable pageable = PageRequest.of(page, size);

        switch (category.toLowerCase()) {
            case "actor":
                searchResults = searchActors(term, pageable);
                break;
            case "show":
                searchResults = searchShows(term, pageable);
                break;
            case "product":
                searchResults = searchProducts(term, pageable);
                break;
            case "user":
                searchResults = mergeUserSearchResults(term, pageable);  // 사용자 검색 병합 처리
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        result.put("results", searchResults.getContent());
        result.put("totalPages", searchResults.getTotalPages());
        result.put("currentPage", searchResults.getNumber() + 1); // 1 기반 페이지로 표시
        result.put("totalElements", searchResults.getTotalElements());

        return result;
    }

    // 사용자 ID와 닉네임 검색 결과를 병합
    private Page<?> mergeUserSearchResults(String term, Pageable pageable) {
        Page<UserDto> searchResultsId = searchUsersId(term, pageable);
        Page<UserDto> searchResultsNickname = searchUsersNickname(term, pageable);

        // 병합할 데이터들
        List<UserDto> mergedResults = new ArrayList<>();
        mergedResults.addAll(searchResultsId.getContent());
        mergedResults.addAll(searchResultsNickname.getContent());

        // 병합된 결과에 대해 다시 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mergedResults.size());
        List<UserDto> paginatedResults = mergedResults.subList(start, end);

        return new PageImpl<>(paginatedResults, pageable, mergedResults.size());
    }

    // 배우 검색
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

    // 작품 검색
    public Page<CharacterDto> searchShows(String term, Pageable pageable) {
        // showsRepository를 사용하여 title을 기반으로 shows를 찾고, 해당 show의 characters를 페이징 처리하여 반환
        Page<Character> charactersPage = showsRepository.findCharactersByShowTitle(term, pageable);
        return charactersPage.map(Character::toDto);
    }

    // 상품 검색
    public Page<ProductDto> searchProducts(String term, Pageable pageable) {
        Page<Product> productsPage = productsRepository.findByNameContainingOrderByNoDesc(term, pageable);
        return productsPage.map(Product::toDto);
    }

    // 사용자 닉네임 검색
    public Page<UserDto> searchUsersNickname(String term, Pageable pageable) {
        Page<User> usersPage = usersRepository.findByNicknameContaining(term, pageable);
        return usersPage.map(User::toDto);
    }

    // 사용자 ID로 검색
    public Page<UserDto> searchUsersId(String term, Pageable pageable) {
        Page<User> usersPage = usersRepository.findByIdContaining(term, pageable);
        return usersPage.map(User::toDto);  // User -> UserDto로 변환
    }

}
