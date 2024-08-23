package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.CommentDto;
import pack.dto.CommentLikeDto;
import pack.dto.FollowDto;
import pack.dto.PostDetailDto;
import pack.dto.PostDto;
import pack.dto.PostLikeDto;
import pack.dto.ReportedPostDto;
import pack.dto.UserDto;
import pack.model.PostsModel;

@RestController
public class PostsController {

	@Autowired
	private PostsModel pm;

	// 유저 정보 한 개 가져오기
	@GetMapping("/posts/user/{no}")
	public UserDto userInfo(@PathVariable("no") int no) {
		return pm.userInfo(no);
	}

	// 유저 정보 수정하기
	@PutMapping("/posts/user/{userNo}")
	public Map<String, Boolean> userInfoUpdate(@PathVariable("userNo") int no, @RequestBody UserDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.userInfoUpdate(no, dto));
		return result;
	}

	// 팔로우 정보 (팔로잉, 팔로워) 가져오기
	@GetMapping("/posts/user/follow/{no}")
	public Map<String, List<Integer>> followInfo(@PathVariable("no") int no) {
		return pm.followInfo(no);
	}

	// 팔로잉 정보 가져오기
	@GetMapping("/posts/user/follow/followee/{no}")
	public ResponseEntity<Page<UserDto>> followeeInfo(@PathVariable("no") int no, Pageable pageable) {
		Page<UserDto> followPage = pm.followeeInfo(no, pageable);
		return ResponseEntity.ok(followPage);
	}

	// 팔로워 정보 가져오기
	@GetMapping("/posts/user/follow/follower/{no}")
	public ResponseEntity<Page<UserDto>> followerInfo(@PathVariable("no") int no, Pageable pageable) {
		Page<UserDto> followPage = pm.followerInfo(no, pageable);
		return ResponseEntity.ok(followPage);
	}

	// 팔로우 여부 체크하기
	@GetMapping("/posts/user/follow/{no}/{fno}")
	public boolean followCheck(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		return pm.followCheck(no, fno);
	}

	// 팔로우 취소하기 (no 가 fno 팔로우 취소)
	@DeleteMapping("/posts/user/follow/{no}/{fno}")
	public Map<String, Boolean> deleteFollow(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deleteFollow(no, fno));
		return result;
	}

	// 팔로우하기
	@PostMapping("/posts/user/follow")
	public Map<String, Boolean> insertFollow(@RequestBody FollowDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertFollow(dto));
		return result;
	}

	// 팔로우한 사람 글 목록 가져오기
	@GetMapping("/posts/followPostList/{no}")
	public ResponseEntity<Page<PostDto>> followPostList(@PathVariable("no") int userNo, Pageable pageable) {
		Page<PostDto> postPage = pm.followPostList(userNo, pageable);
		return ResponseEntity.ok(postPage);
	}

	// 특정 유저 글 목록 가져오기
	@GetMapping("/posts/list/{no}")
	public ResponseEntity<Page<PostDto>> postListByUser(@PathVariable("no") int no, Pageable pageable) {
		Page<PostDto> postPage = pm.postListByUser(no, pageable);
		return ResponseEntity.ok(postPage);
	}

	// 게시글 상세 보기
	@GetMapping("/posts/detail/{no}")
	public ResponseEntity<PostDetailDto> postDetail(@PathVariable("no") int postNo, Pageable pageable) {
		PostDetailDto postDetailPage = pm.postDetail(postNo, pageable);
		return ResponseEntity.ok(postDetailPage);
	}

	// 게시글 좋아요 갯수 가져오기
	@GetMapping("/posts/postlike/{no}")
	public Map<String, Integer> getPostLike(@PathVariable("no") int no) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("result", pm.getPostLike(no).size());
		return result;
	}

	// 댓글 좋아요 가져오기
	@GetMapping("/posts/commentlike/{no}")
	public Map<String, Integer> getCommentLike(@PathVariable("no") int no) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("result", pm.getCommentLike(no).size());
		return result;
	}

	// 게시글 좋아요 취소하기
	@DeleteMapping("/posts/postlike/{postNo}/{userNo}")
	public Map<String, Boolean> deletePostLike(@PathVariable("postNo") int postNo, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deletePostLike(postNo, userNo));
		return result;
	}

	// 게시글 좋아요 하기
	@PostMapping("/posts/postlike")
	public Map<String, Boolean> insertPostLike(@RequestBody PostLikeDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertPostLike(dto));
		return result;
	}

	// 댓글 좋아요 취소하기
	@DeleteMapping("/posts/commentlike/{commentNo}/{userNo}")
	public Map<String, Boolean> deleteCommentLike(@PathVariable("commentNo") int commentNo,
			@PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deleteCommentLike(commentNo, userNo));
		return result;
	}

	// 댓글 좋아요 하기
	@PostMapping("/posts/commentlike")
	public Map<String, Boolean> insertCommentLike(@RequestBody CommentLikeDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertCommentLike(dto));
		return result;
	}

	// 게시글 좋아요 여부 체크하기
	@GetMapping("/posts/postlike/check/{postNo}/{userNo}")
	public Map<String, Boolean> checkPostLike(@PathVariable("postNo") int postNo, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.checkPostLike(postNo, userNo));
		return result;
	}

	// 댓글 좋아요 여부 체크하기
	@GetMapping("/posts/commentlike/check/{commentNo}/{userNo}")
	public Map<String, Boolean> checkCommentLike(@PathVariable("commentNo") int commentNo,
			@PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.checkCommentLike(commentNo, userNo));
		return result;
	}

	// 게시글 삭제하기
	@DeleteMapping("/posts/detail/{postNo}")
	public Map<String, Boolean> deletePosts(@PathVariable("postNo") int postNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deletePosts(postNo));
		return result;
	}

	// 게시글 여러 개 삭제하기
	@DeleteMapping("/posts/delete")
	public Map<String, Boolean> deletePosts(@RequestBody List<Integer> postIds) {
		Map<String, Boolean> result = new HashMap<>();
		result.put("result", pm.deletePosts(postIds));
		return result;
	}

	// 게시글 등록하기
	@PostMapping("/posts/detail")
	public Map<String, Boolean> insertPosts(@RequestBody PostDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertPosts(dto));
		return result;
	}

	// 게시글 수정하기
	@PutMapping("/posts/detail/{postNo}")
	public Map<String, Boolean> updatePosts(@PathVariable("postNo") int postNo, @RequestBody PostDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.updatePosts(postNo, dto));
		return result;
	}

	// 댓글 삭제하기
	@DeleteMapping("/posts/comment/{commentNo}")
	public Map<String, Boolean> deleteComment(@PathVariable("commentNo") int commentNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deleteComment(commentNo));
		return result;
	}

	// 댓글 입력하기
	@PostMapping("/posts/comment")
	public Map<String, Boolean> insertComment(@RequestBody CommentDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertComment(dto));
		return result;
	}

	// 게시글 신고하기
	@PostMapping("/posts/report")
	public Map<String, Boolean> reportedPost(@RequestBody ReportedPostDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.reportedPost(dto));
		return result;
	}

}
