package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import pack.dto.PostDto;
import pack.dto.ProductDto;
import pack.entity.Post;
import pack.entity.Product;
import pack.entity.ReportedPost;
import pack.repository.PostsRepository;
import pack.repository.ReportedPostsRepository;

@Repository
public class AdminCommunityModel {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private ReportedPostsRepository reportedPostsRepository;
    
//    // 페이징된 상품 리스트 조회 메서드
//    public Page<ProductDto> listAll(Pageable pageable) {
//        Page<Product> products = productReposi.findAll(pageable);
//        return products.map(Product::toDto);
//    }
//
//    public Page<PostDto> listAll(Pageable pageable){
//    	Page<Post> post = postsRepository.findAll(pageable);
//    	return post.map(Post::toDto);
//    }
    // 전체 글 조회 메서드
    public List<Post> getAllPosts() {
        return postsRepository.findAll(); // 전체 글 조회
    }
    // 신고 글 조회 메서드
    public List<ReportedPost> getAllReporte() {
        return reportedPostsRepository.findAll(); // 전체 글 조회
    }
}
