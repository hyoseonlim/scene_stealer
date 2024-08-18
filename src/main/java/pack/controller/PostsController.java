package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.CharacterLikeDto;
import pack.dto.CommentLikeDto;
import pack.dto.FollowDto;
import pack.dto.PostDetailDto;
import pack.dto.PostDto;
import pack.dto.PostLikeDto;
import pack.dto.UserDto;
import pack.model.PostsModel;

@RestController
public class PostsController {

	@Autowired
	private PostsModel pm;

	@GetMapping("/posts/user/{no}")
	public UserDto userInfo(@PathVariable("no") int no) {
		return pm.userInfo(no);
	}

	@GetMapping("/posts/user/follow/{no}")
	public Map<String, List<Integer>> followInfo(@PathVariable("no") int no) {
		return pm.followInfo(no);
	}

	@GetMapping("/posts/user/follow/followee/{no}")
	public List<UserDto> followeeInfo(@PathVariable("no") int no) {
		return pm.followeeInfo(no);
	}

	@GetMapping("/posts/user/follow/follower/{no}")
	public List<UserDto> followerInfo(@PathVariable("no") int no) {
		return pm.followerInfo(no);
	}

	@GetMapping("/posts/user/follow/{no}/{fno}")
	public boolean followCheck(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		return pm.followCheck(no, fno);
	}

	@DeleteMapping("/posts/user/follow/{no}/{fno}")
	public Map<String, Boolean> deleteFollow(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deleteFollow(no, fno));
		return result;
	}

	@PostMapping("/posts/user/follow")
	public Map<String, Boolean> insertFollow(@RequestBody FollowDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertFollow(dto));
		return result;
	}

	@GetMapping("/posts/followPostList/{no}")
	public List<PostDto> followPostList(@PathVariable("no") int userNo) {
		return pm.followPostList(userNo);
	}

	@GetMapping("/posts/list/{no}")
	public List<PostDto> postListByUser(@PathVariable("no") int no) {
		return pm.postListByUser(no);
	}

	@GetMapping("/posts/detail/{no}")
	public PostDetailDto postDetail(@PathVariable("no") int postNo) {
		return pm.postDetail(postNo);
	}

	@GetMapping("/posts/postlike/{no}")
	public Map<String, Integer> getPostLike(@PathVariable("no") int no) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("result", pm.getPostLike(no).size());
		return result;
	}

	@GetMapping("/posts/commentlike/{no}")
	public Map<String, Integer> getCommentLike(@PathVariable("no") int no) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("result", pm.getCommentLike(no).size());
		return result;
	}

	@DeleteMapping("/posts/postlike/{postNo}/{userNo}")
	public Map<String, Boolean> deletePostLike(@PathVariable("postNo") int postNo, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deletePostLike(postNo, userNo));
		return result;
	}

	@PostMapping("/posts/postlike")
	public Map<String, Boolean> insertPostLike(@RequestBody PostLikeDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertPostLike(dto));
		return result;
	}

	@DeleteMapping("/posts/commentlike/{commentNo}/{userNo}")
	public Map<String, Boolean> deleteCommentLike(@PathVariable("commentNo") int commentNo,
			@PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.deleteCommentLike(commentNo, userNo));
		return result;
	}

	@PostMapping("/posts/commentlike")
	public Map<String, Boolean> insertCommentLike(@RequestBody CommentLikeDto dto) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.insertCommentLike(dto));
		return result;
	}
	
	@GetMapping("/posts/postlike/check/{postNo}/{userNo}")
	public Map<String, Boolean> checkPostLike(@PathVariable("postNo") int postNo, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.checkPostLike(postNo, userNo));
		return result;
	}
	
	@GetMapping("/posts/commentlike/check/{commentNo}/{userNo}")
	public Map<String, Boolean> checkCommentLike(@PathVariable("commentNo") int commentNo, @PathVariable("userNo") int userNo) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", pm.checkCommentLike(commentNo, userNo));
		return result;
	}
}
