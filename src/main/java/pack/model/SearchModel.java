package pack.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.entity.Actor;
import pack.entity.Show;
import pack.repository.ActorsRepository;
import pack.repository.ShowsRepository;

@Repository
public class SearchModel {
    @Autowired
    private ActorsRepository actorsRepository;
    
    @Autowired
    private ShowsRepository showsRepository;
    
    public List<?> search(String category, String term) {
        switch (category.toLowerCase()) {
            case "actor":
            	System.out.println("actor : " + searchActors(term));
                return searchActors(term);
            case "show":
            	System.out.println("show : " + searchShows(term));
                return searchShows(term);
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
}