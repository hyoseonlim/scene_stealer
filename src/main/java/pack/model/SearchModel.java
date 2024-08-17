package pack.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.ActorDto;
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
    
    private List<Actor> searchActors(String term) {
        return actorsRepository.findByNameContaining(term);
    }
    
    private List<Show> searchShows(String term) {
        return showsRepository.findByTitleContaining(term);
    }

    private List<Product> searchProducts(String term) {
    	return productsRepository.findByNameContaining(term);
    }
    
    
//    public Optional<Actor> getActorData(int id) {
//        return actorsRepository.findById(id);
//    }
//    
//    public Optional<Show> getShowData(int id) {
//        return showsRepository.findById(id);
//    }
    
    // 이름으로 배우를 찾는 메서드 추가
    public Optional<Actor> findActorByName(String name) {
        return actorsRepository.findByName(name);
    }
}