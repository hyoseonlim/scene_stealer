package pack.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pack.admin.model.AdminCommunityModel;
import pack.dto.PostDto;
import pack.dto.ReportedPosts_a;

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
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> postPage = adminCommunityModel.getAllPosts(pageable);
        
        return ResponseEntity.ok(postPage);
    }

    // 페이징된 신고 글 조회 
    @GetMapping("/posts/reported")
    public ResponseEntity<Page<ReportedPosts_a>> getReportedPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "latest") String sort) {

        Page<ReportedPosts_a> reportedPostsPage;
        Pageable pageable = PageRequest.of(page, size);

        if ("mostReported".equals(sort)) {
            reportedPostsPage = adminCommunityModel.getMostReportedPosts(pageable);
        } else {
            reportedPostsPage = adminCommunityModel.getAllReportedPosts(pageable);
        }

        return ResponseEntity.ok(reportedPostsPage);
    }

    // 신고글 삭제 기능
    @DeleteMapping("/posts/{no}")
    public Map<String, Object> deleteReportedPost(@PathVariable("no") int no) {
        Map<String, Object> response = new HashMap<>();
        adminCommunityModel.deletePostData(no);
        response.put("isSuccess", true);
        return response;
    }
}
