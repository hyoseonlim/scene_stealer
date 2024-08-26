package pack.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.model.AdminCommunityModel;
import pack.dto.PostDto;
import pack.dto.ReportedPostDto;
import pack.dto.ReportedPosts_a;
import pack.entity.Post;
import pack.entity.ReportedPost;

@RestController
@RequestMapping("/admin")
public class AdminCommunityController {

    @Autowired
    private AdminCommunityModel adminCommunityModel;
    
//    @GetMapping
//    public ResponseEntity<Page<PostDto>> getAllPost(
//    		@RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size){
//    	Pageable pageable = PageRequest.of(page, size);
//    	Page<PostDto> page2;
//    	return ResponseEntity.ok(page2);
//    	
//    }

    // 전체 글 조회 
    @GetMapping("/posts")
    public List<PostDto> getAllPosts() { 
        List<Post> posts = adminCommunityModel.getAllPosts();
        return posts.stream()
                    .map(Post::toDto) // Post 엔티티를 PostDto로 변환
                    .collect(Collectors.toList());
    }

    // 전체 신고 글 조회 
    @GetMapping("/posts/reported")
    public ArrayList<ReportedPosts_a> getReportedPosts() { 
        List<ReportedPost> reportedPosts = adminCommunityModel.getAllReporte();
        ArrayList<ReportedPosts_a> reportedPosts_a = new ArrayList<ReportedPosts_a>();
       for(ReportedPost entity: reportedPosts) {
    	   ReportedPosts_a dto = new ReportedPosts_a();
    	   dto.setNo(entity.getNo());			// entity.getNo
    	   dto.setUserId(entity.getUser().getId());
    	   dto.setCategory(entity.getCategory());    //신고 사유 getCat
    	   dto.setContent(entity.getPost().getContent());	// 글 내용
    	   dto.setReportsCount(entity.getPost().getReportsCount());
    	   reportedPosts_a.add(dto);// 신고 횟수
       }
       return reportedPosts_a;
    }
}
