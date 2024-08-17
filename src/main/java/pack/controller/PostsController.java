package pack.controller;

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
import pack.dto.FollowDto;
import pack.dto.PostDto;
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
	
	@GetMapping("/posts/user/follow/check/{no}/{fno}")
	public boolean followCheck(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		return pm.followCheck(no, fno);
	}
	
	@DeleteMapping("/posts/user/follow/delete/{no}/{fno}")
	public boolean deleteFollow(@PathVariable("no") int no, @PathVariable("fno") int fno) {
		return pm.deleteFollow(no, fno);
	}
	
	@PostMapping("/posts/user/follow/insert")
	public boolean insertFollow(@RequestBody FollowDto dto) {
		return pm.insertFollow(dto);
	}
	
	@GetMapping("/posts/followPostList/{no}")
	public List<PostDto> followPostList(@PathVariable("no") int userNo) {
		return pm.followPostList(userNo);
	}
}
