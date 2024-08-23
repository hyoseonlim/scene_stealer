// 커뮤니티 관리 (전체 글 & 신고된 글 관리)
package pack.admin.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.model.AdminCommunityModel;
import pack.dto.PostDto;
import pack.entity.Post;

@RestController
@RequestMapping("/admin")
public class AdminCommunityController {

    @Autowired
    private AdminCommunityModel adminCommunityModel;

    // 전체 글 조회 엔드포인트
    @GetMapping("/posts")
    public List<PostDto> getList() { 
        // DB에서 모든 글을 조회하고, Post 엔티티를 PostDto로 변환하여 반환
        List<Post> posts = adminCommunityModel.getAllPosts();
        return posts.stream()
                    .map(Post::toDto) // Post 엔티티를 PostDto로 변환
                    .collect(Collectors.toList());
    }
}
