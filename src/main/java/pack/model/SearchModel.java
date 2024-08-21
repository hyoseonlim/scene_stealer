package pack.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.ActorDto;
import pack.dto.ProductDto;
import pack.dto.ShowDto;
import pack.entity.Actor;
import pack.entity.Product;
import pack.entity.Show;
import pack.repository.ActorsRepository;
import pack.repository.ProductsRepository;
import pack.repository.ShowsRepository;

@Repository
public class SearchModel {
    @Autowired
    private ActorsRepository actorsRepository;
    
    @Autowired
    private ShowsRepository showsRepository;
 
    @Autowired
    private ProductsRepository productsRepository;
    
    
    // 자동완성 ============================================================
    public List<?> autocomplete(String category, String term) {
        switch (category.toLowerCase()) {
            case "actor":
                return searchActors(term);
            case "show":
                return searchShows(term);
            case "product":
                return searchProducts(term);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
    
    public List<ActorDto> searchActors(String term) {
        return actorsRepository.findByNameContaining(term).stream().map(Actor::toDto).toList();
    }
    
    public List<ShowDto> searchShows(String term) {
        return showsRepository.findByTitleContaining(term).stream().map(Show::toDto).toList();
    }

    public List<ProductDto> searchProducts(String term) {
        return productsRepository.findByNameContaining(term).stream().map(Product::toDto).toList();
    }
    
    public List<ShowDto> findShowsByActorName(String name) {
        // 1. 이름으로 배우를 검색하여 ID 목록을 가져옴
        List<Actor> actors = actorsRepository.findByNameContaining(name);
        List<Integer> actorNos = actors.stream().map(Actor::getNo).collect(Collectors.toList());

        // 2. 각 배우 ID로 쇼 ID 목록을 가져옴
        List<Integer> showNos = actorNos.stream()
            .flatMap(actorNo -> actorsRepository.findShowsByActorNo(actorNo).stream())
            .distinct()
            .collect(Collectors.toList());
        
        // 3. 쇼 ID 목록을 사용하여 쇼 객체를 가져옴
        List<Show> shows = showsRepository.findByShowNos(showNos);

        // 4. 쇼 객체를 DTO로 변환하여 반환
        return shows.stream().map(Show::toDto).collect(Collectors.toList());
    }
}