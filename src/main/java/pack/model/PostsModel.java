package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.CommentDto;
import pack.dto.CommentLikeDto;
import pack.dto.FollowDto;
import pack.dto.PostDetailDto;
import pack.dto.PostDto;
import pack.dto.PostLikeDto;
import pack.dto.UserDto;
import pack.entity.Comment;
import pack.entity.CommentLike;
import pack.entity.Follow;
import pack.entity.Post;
import pack.entity.PostLike;
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
		List<Integer> followerList = frps.findByFolloweeNo(no).stream().map(f -> f.getFollower().getNo())
				.collect(Collectors.toList());
		List<Integer> followeeList = frps.findByFollowerNo(no).stream().map(f -> f.getFollowee().getNo())
				.collect(Collectors.toList());
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		result.put("followerList", followerList);
		result.put("followeeList", followeeList);
		return result;
	}

	public List<UserDto> followeeInfo(int no) {
		return frps.findByFollowerNo(no).stream().map(Follow::getFollowee).map(User::toDto)
				.collect(Collectors.toList());
	}

	public List<UserDto> followerInfo(int no) {
		return frps.findByFolloweeNo(no).stream().map(Follow::getFollower).map(User::toDto)
				.collect(Collectors.toList());
	}

	public boolean followCheck(int no, int fno) {
		return frps.findByFollowerNoAndFolloweeNo(no, fno).size() > 0 ? true : false;
	}

	@Transactional
	public boolean deleteFollow(int no, int fno) {
		boolean b = false;
		try {
			if (frps.deleteByFolloweeNoAndFollowerNo(fno, no) > 0) {
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

	public List<PostDto> followPostList(int userNo) {
		List<Integer> followeeList = frps.findByFollowerNo(userNo).stream().map(f -> f.getFollowee().getNo())
				.collect(Collectors.toList());

		List<PostDto> postList = new ArrayList<>();

		for (Integer i : followeeList) {
			List<PostDto> posts = prps.findByUserNo(i).stream().map(Post::toDto).collect(Collectors.toList());
			postList.addAll(posts);
		}

		return postList;
	}

	public List<PostDto> postListByUser(int no) {
		return prps.findByUserNo(no).stream().map(Post::toDto).collect(Collectors.toList());
	}

	public PostDetailDto postDetail(int postNo) {

		PostDto postInfo = Post.toDto(prps.findById(postNo).get());
		UserDto userInfo = User.toDto(urps.findById(postInfo.getUserNo()).get());

		return PostDetailDto.builder().posts(postInfo).userPic(userInfo.getPic()).userNickname(userInfo.getNickname())
				.comments(crps.findByPostNo(postNo).stream().map(Comment::toDto).collect(Collectors.toList())).build();

	}

	@Transactional
	public boolean deletePostLike(int postNo, int userNo) {
		boolean b = false;
		try {
			if (plrps.deleteByPostNoAndUserNo(postNo, userNo) > 0) {
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deletePostLike ERROR : " + e.getMessage());
		}
		return b;

	}

	@Transactional
	public boolean insertPostLike(PostLikeDto dto) {
		try {
			plrps.save(PostLikeDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertPostLike ERROR : " + e.getMessage());
			return false;
		}
	}

	@Transactional
	public boolean deleteCommentLike(int commentNo, int userNo) {
		boolean b = false;
		try {
			if (clrps.deleteByCommentNoAndUserNo(commentNo, userNo) > 0) {
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deleteCommentLike ERROR : " + e.getMessage());
		}
		return b;
	}

	@Transactional
	public boolean insertCommentLike(CommentLikeDto dto) {
		try {
			clrps.save(CommentLikeDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertCommentLike ERROR : " + e.getMessage());
			return false;
		}
	}

	public List<PostLikeDto> getPostLike(int no) {
		return plrps.findByPostNo(no).stream().map(PostLike::toDto).collect(Collectors.toList());
	}

	public List<CommentLikeDto> getCommentLike(int no) {
		return clrps.findByCommentNo(no).stream().map(CommentLike::toDto).collect(Collectors.toList());
	}

	public boolean checkPostLike(int postNo, int userNo) {
		return plrps.findByPostNoAndUserNo(postNo, userNo).size() == 0 ? false : true;
	}
	
	public boolean checkCommentLike(int commentNo, int userNo) {
		return clrps.findByCommentNoAndUserNo(commentNo, userNo).size() == 0 ? false : true;
	}
	
	@Transactional
	public boolean deletePosts(int postNo) {
		boolean b = false;
		try {
			if (prps.deleteByNo(postNo) > 0) {
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deletePosts ERROR : " + e.getMessage());
		}
		return b;
	}

	@Transactional
	public boolean insertPosts(PostDto dto) {
		try {
			prps.save(PostDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertPosts ERROR : " + e.getMessage());
			return false;
		}
	}
	
	@Transactional
	public boolean deleteComment(int commentNo) {
		boolean b = false;
		try {
			if (crps.deleteByNo(commentNo) > 0) {
				b = true;
			}
		} catch (Exception e) {
			System.out.println("deleteComment ERROR : " + e.getMessage());
		}
		return b;
	}

	@Transactional
	public boolean insertComment(CommentDto dto) {
		try {
			crps.save(CommentDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("insertComment ERROR : " + e.getMessage());
			return false;
		}
	}

}
