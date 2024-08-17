package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.CharacterLikeDto;
import pack.dto.FollowDto;
import pack.dto.UserDto;
import pack.entity.Follow;
import pack.entity.User;
import pack.repository.CommentLikeRepository;
import pack.repository.CommentsRepository;
import pack.repository.FollowsRepository;
import pack.repository.PostLikeRepository;
import pack.repository.PostsRepository;
import pack.repository.UsersRepository;

@Repository
public class PostsModel {

	@Autowired
	private UsersRepository urps;

	@Autowired
	private PostsRepository prps;

	@Autowired
	private CommentsRepository crps;

	@Autowired
	private PostLikeRepository plrps;

	@Autowired
	private CommentLikeRepository clrps;

	@Autowired
	private FollowsRepository frps;

	public UserDto userInfo(int no) {
		return User.toDto(urps.findById(no).get());
	}

	public Map<String, List<Integer>> followInfo(int no) {
		List<Integer> followerList = frps.findByFolloweeNo(no).stream().map(f -> f.getFollower().getNo()).collect(Collectors.toList());
		List<Integer> followeeList = frps.findByFollowerNo(no).stream().map(f -> f.getFollowee().getNo()).collect(Collectors.toList());
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		result.put("followerList", followerList);
		result.put("followeeList", followeeList);
		return result;
	}
	
	public List<UserDto> followeeInfo(int no) {
		return frps.findByFollowerNo(no).stream().map(Follow::getFollowee).map(User::toDto).collect(Collectors.toList());
	}
	
	public List<UserDto> followerInfo(int no) {
		return frps.findByFolloweeNo(no).stream().map(Follow::getFollower).map(User::toDto).collect(Collectors.toList());
	}
	
	public boolean followCheck(int no, int fno) {
		return frps.findByFollowerNoAndFolloweeNo(no, fno).size() > 0 ? true : false;
	}
	
	@Transactional
	public boolean deleteFollow(int no, int fno) {
		boolean b = false;
		try {
			if(frps.deleteByFolloweeNoAndFollowerNo(fno, no) > 0) {				
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deleteFollow ERROR : " + e.getMessage());
		}
		return b;
		
	}
	
	@Transactional
	public boolean insertFollow(FollowDto dto) {
		try {
			frps.save(FollowDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertScrap ERROR : " + e.getMessage());
			return false;
		}
	}

 
}
