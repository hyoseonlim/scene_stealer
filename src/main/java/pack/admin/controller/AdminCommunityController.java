package pack.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.model.AdminCommunityModel;
import pack.dto.PostDto;
import pack.dto.ReportedPostDto;

@RestController
@RequestMapping("/admin")
public class AdminCommunityController {

    @Autowired
    private AdminCommunityModel adminCommunityModel;

    // 페이징된 전체 글 조회 
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // 최신순으로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<PostDto> postPage = adminCommunityModel.getAllPosts(pageable);
        return ResponseEntity.ok(postPage);
    }

    // 페이징된 신고 글 조회 
    @GetMapping("/posts/reported")
    public ResponseEntity<Page<PostDto>> getReportedPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "latest") String sort) {

    	System.out.println("hi4");
        Page<PostDto> reportedPostsPage;
        Pageable pageable = PageRequest.of(page, size);

        System.out.println("hi5");
        if ("mostReported".equals(sort)) {
            reportedPostsPage = adminCommunityModel.getMostReportedPosts(pageable);
        } else {
            reportedPostsPage = adminCommunityModel.getAllReportedPosts(pageable);
        }
        System.out.println("hi6");
        return ResponseEntity.ok(reportedPostsPage);
    }
    
    @GetMapping("/posts/reportedInfos")
    public List<ReportedPostDto> getReportedInfos() {
    	return adminCommunityModel.getReportedInfos();
    }

    // 신고글 삭제 기능
    @DeleteMapping("/posts/{no}")
    public Map<String, Object> deleteReportedPost(@PathVariable("no") int no) {
        Map<String, Object> response = new HashMap<>();
        adminCommunityModel.deletePostData(no);
        response.put("isSuccess", true);
        return response;
    }
    @GetMapping("/posts/detail/{no}")
    public ResponseEntity<PostDto> getPostDetail(@PathVariable("no") int no) {
        PostDto postDto = adminCommunityModel.getPostDetail(no);
        if (postDto != null) {
            return ResponseEntity.ok(postDto);
        }
        return ResponseEntity.notFound().build(); // 게시물이 없을 경우
    }

}
