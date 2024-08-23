package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.entity.Post;
import pack.entity.ReportedPost;
import pack.repository.PostsRepository;
import pack.repository.ReportedPostsRepository;

@Repository
public class AdminCommunityModel {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private ReportedPostsRepository reportedPostsRepository;

    // 전체 글 조회 메서드
    public List<Post> getAllPosts() {
        return postsRepository.findAll(); // 전체 글 조회
    }
    // 신고 글 조회 메서드
    public List<ReportedPost> getAllReporte() {
        return reportedPostsRepository.findAll(); // 전체 글 조회
    }
}
