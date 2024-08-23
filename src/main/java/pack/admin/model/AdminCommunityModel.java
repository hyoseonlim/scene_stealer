package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.entity.Post;
import pack.repository.PostsRepository;

@Repository
public class AdminCommunityModel {

    @Autowired
    private PostsRepository postsRepository;

    // 전체 글 조회 메서드
    public List<Post> getAllPosts() {
        return postsRepository.findAll(); // 전체 글 조회
    }
}
