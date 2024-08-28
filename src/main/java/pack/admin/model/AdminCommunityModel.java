package pack.admin.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pack.dto.PostDto;
import pack.dto.ReportedPostDto;
import pack.entity.Comment;
import pack.entity.Post;
import pack.entity.ReportedPost;
import pack.repository.CommentLikeRepository;
import pack.repository.CommentsRepository;
import pack.repository.PostLikeRepository;
import pack.repository.PostsRepository;
import pack.repository.ReportedPostsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AdminCommunityModel {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private ReportedPostsRepository reportedPostsRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postsRepository.findAll(pageable).map(this::convertToPostDto);
    }

    public Page<PostDto> getAllReportedPosts(Pageable pageable) {
        return postsRepository.findByReportsCountGreaterThan(0, pageable).map(Post::toDto);
    }

    public Page<PostDto> getMostReportedPosts(Pageable pageable) {
        return postsRepository.findByReportsCountGreaterThanOrderByReportsCountDesc(0, pageable).map(Post::toDto);
    }
    
    public List<ReportedPostDto> getReportedInfos(){
    	return reportedPostsRepository.findAll().stream().map(ReportedPost::toDto).toList();
    }
    
    /*
    public String deleteReportedPostByUserId(String userid) {
        List<ReportedPost> reportedPosts = reportedPostsRepository.findByUser_Id(userid);
        if (!reportedPosts.isEmpty()) {
            reportedPostsRepository.deleteAll(reportedPosts);
            return "신고글 삭제 성공";
        } else {
            return "해당 유저의 신고글을 찾을 수 없습니다.";
        }
    }
    */

    // Post 엔티티를 PostDto로 변환하는 메서드
    private PostDto convertToPostDto(Post post) {
        return PostDto.builder()
            .no(post.getNo())
            .content(post.getContent())
            .date(post.getDate())
            .pic(post.getPic())
            .userId(post.getUser().getId())
            .likesCount(post.getLikesCount())
            .commentsCount(post.getCommentsCount())
            .reportsCount(post.getReportsCount())
            .reportedPostsList(post.getReportedPosts().stream().map(ReportedPost::getNo).collect(Collectors.toList()))
            .productNo(post.getProduct() != null ? post.getProduct().getNo() : null)
            .userNickname(post.getUser().getNickname())
            .userNo(post.getUser().getNo())
            .userPic(post.getUser().getPic())
            .commentsList(post.getComments().stream().map(Comment::getNo).collect(Collectors.toList()))
            .build();
    }
    
    @Transactional
    public void deletePostData(int no) { // Post PK로 5개 테이블 처리
    	// Comment_like
    	commentsRepository.findByPostNo(no).stream()
        	.map(Comment::getNo) // 각 댓글의 no 값 추출
        	.forEach(commentNo -> commentLikeRepository.deleteByCommentNo(commentNo));
    	// Comment
    	commentsRepository.deleteByPostNo(no);
    	// Post_like
    	postLikeRepository.deleteByPostNo(no);
    	// Reported_post
    	reportedPostsRepository.deleteByPostNo(no);
    	// Post
    	postsRepository.deleteById(no);
    }
}
