package pack.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.PostDto;
import pack.dto.ReviewDto;
import pack.dto.ShowDto;
import pack.entity.Post;
import pack.entity.Review;
import pack.entity.Show;
import pack.repository.PostsRepository;
import pack.repository.ReviewsRepository;
import pack.repository.ShowsRepository;

@Repository
public class MainModel {
	
	@Autowired
	private ShowsRepository srps;
	
	@Autowired
	private ReviewsRepository rrps;
	
	@Autowired
	private PostsRepository prps;
	
	public List<ShowDto> mainShowData() {
		return srps.findShowAll().stream().map(Show::toDto).toList();
	}
	
	public List<ReviewDto> mainShowReview() {
		return rrps.findTop3ByOrderByNoDesc().stream().map(Review::toDto).toList();
	} 
	
	public List<PostDto> mainShowPosts() {
		return prps.findTop3ByOrderByNoDesc().stream().map(Post::toDto).toList();
	}
	
	

}
