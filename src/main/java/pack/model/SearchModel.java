package pack.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pack.dto.ActorDto;
import pack.dto.ProductDto;
import pack.dto.ShowDto;
import pack.dto.UserDto;
import pack.entity.Actor;
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
        return showsRepository.findByTitleContaining(term, Pageable.unpaged()).stream()
            .map(Show::toDto)
            .toList();
    }
    
    public List<ProductDto> autocompleteProducts(String term) {
        return productsRepository.findByNameContaining(term).stream()
            .map(Product::toDto)
            .toList();
    }

    // 페이징된 검색 메서드
    public Page<Actor> searchActors(String term, Pageable pageable) {
        return actorsRepository.findByNameContaining(term, pageable);
    }

    public Page<ShowDto> searchShows(String term, Pageable pageable) {
        Page<Show> showsPage = showsRepository.findByTitleContaining(term, pageable);
        return showsPage.map(Show::toDto);
    }

    public Page<ProductDto> searchProducts(String term, Pageable pageable) {
        Page<Product> productsPage = productsRepository.findByNameContaining(term, pageable);
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