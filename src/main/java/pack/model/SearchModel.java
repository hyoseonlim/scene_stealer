package pack.model;

import java.util.List;
import java.util.Optional;

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
    
    public List<?> search(String category, String term) {
        switch (category.toLowerCase()) {
            case "actor":
            	System.out.println("actor : " + searchActors(term));
                return searchActors(term);
            case "show":
            	System.out.println("show : " + searchShows(term));
                return searchShows(term);
            case "product":
            	System.out.println("product : " + searchProducts(term));
            	return searchProducts(term);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
    
    private List<ActorDto> searchActors(String term) {
        return actorsRepository.findByNameContaining(term).stream().map(Actor::toDto).toList();
    }
    
    private List<ShowDto> searchShows(String term) {
        return showsRepository.findByTitleContaining(term).stream().map(Show::toDto).toList();
    }

    private List<ProductDto> searchProducts(String term) {
    	return productsRepository.findByNameContaining(term).stream().map(Product::toDto).toList();
    }
    
    
    // 작품 번호 리스트를 받아서 작품의 정보를 조회...
    public List<ShowDto> getShowsByNos(List<Integer> showNos) {
        return showsRepository.findByShowNos(showNos).stream().map(Show::toDto).toList();
    }
    
    public List<Show> getShowsByActorNo(int actorNo) {
        List<Integer> showNos = actorsRepository.findShowNosByActorNo(actorNo);
        System.out.println("Show Nos: " + showNos);  // 디버깅용 로그
        return showsRepository.findByShowNos(showNos);
    }
    
    
//    public Optional<Actor> getActorData(int id) {
//        return actorsRepository.findById(id);
//    }
//    
//    public Optional<Show> getShowData(int id) {
//        return showsRepository.findById(id);
//    }
    
    // 이름으로 배우를 찾는 메서드 추가
//    public Optional<Actor> findActorByName(String name) {
//        return actorsRepository.findByName(name);
//    }
}