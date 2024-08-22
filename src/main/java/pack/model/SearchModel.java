package pack.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    
    // 자동완성 기능을 처리하는 메서드입니다.
    public List<?> autocomplete(String category, String term) {
        // 카테고리에 따라 적절한 검색 메서드를 호출합니다.
        switch (category.toLowerCase()) {
            case "actor":
                return searchActors(term); // 배우 검색
            case "show":
                return searchShows(term); // 쇼 검색
            case "product":
                return searchProducts(term); // 상품 검색
            default:
                // 유효하지 않은 카테고리인 경우 예외를 발생시킵니다.
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
    
    // 배우 이름으로 검색하여 결과를 DTO 형태로 반환합니다.
    public List<ActorDto> searchActors(String term) {
        // 이름에 검색어를 포함하는 배우를 찾고, DTO로 변환합니다.
        return actorsRepository.findByNameContaining(term).stream()
            .map(Actor::toDto)
            .toList();
    }
    
    // 쇼 제목으로 검색하여 결과를 DTO 형태로 반환합니다.
    public List<ShowDto> searchShows(String term) {
        // 제목에 검색어를 포함하는 쇼를 찾고, DTO로 변환합니다.
        return showsRepository.findByTitleContaining(term).stream()
            .map(Show::toDto)
            .toList();
    }

    // 상품 이름으로 검색하여 결과를 DTO 형태로 반환합니다.
    public List<ProductDto> searchProducts(String term) {
        // 이름에 검색어를 포함하는 상품을 찾고, DTO로 변환합니다.
        return productsRepository.findByNameContaining(term).stream()
            .map(Product::toDto)
            .toList();
    }
    
    // 사용자 닉네임으로 검색하여 결과를 DTO 형태로 반환합니다.
    public List<UserDto> searchUsersNickname(String term) {
        // 닉네임에 검색어를 포함하는 사용자를 찾고, DTO로 변환합니다.
        return usersRepository.findByNicknameContaining(term).stream()
            .map(User::toDto)
            .toList();
    }

    // 사용자 ID로 검색하여 결과를 DTO 형태로 반환합니다.
    public List<UserDto> searchUsersId(String term) {
        System.out.println("Searching users with id containing: " + term);
        // ID에 검색어를 포함하는 사용자들을 찾습니다.
        List<User> users = usersRepository.findByIdContaining(term);
        System.out.println("Found users: " + users);
        // 찾은 사용자들을 DTO로 변환합니다.
        return users.stream()
            .map(User::toDto)
            .toList();
    }
    
    // 배우 이름으로 쇼를 검색하여 결과를 DTO 형태로 반환합니다.
    public List<ShowDto> findShowsByActorName(String name) {
        // 1. 배우 이름으로 해당 배우를 검색하여 배우 ID 목록을 가져옵니다.
        List<Actor> actors = actorsRepository.findByNameContaining(name);
        List<Integer> actorNos = actors.stream()
            .map(Actor::getNo)
            .collect(Collectors.toList());

        // 2. 각 배우 ID로 쇼 ID 목록을 가져옵니다.
        List<Integer> showNos = actorNos.stream()
            .flatMap(actorNo -> actorsRepository.findShowsByActorNo(actorNo).stream())
            .distinct()
            .collect(Collectors.toList());
        
        // 3. 쇼 ID 목록을 사용하여 쇼 객체를 가져옵니다.
        List<Show> shows = showsRepository.findByShowNos(showNos);

        // 4. 쇼 객체를 DTO로 변환하여 반환합니다.
        return shows.stream()
            .map(Show::toDto)
            .collect(Collectors.toList());
    }
}