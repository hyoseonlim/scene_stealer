package pack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import pack.dto.CommentDto;
import pack.dto.CommentLikeDto;
import pack.dto.FollowDto;
import pack.dto.PostDetailDto;
import pack.dto.PostDto;
import pack.dto.PostLikeDto;
import pack.dto.ProductDto;
import pack.dto.ReportedPostDto;
import pack.dto.UserDto;
import pack.entity.Comment;
import pack.entity.CommentLike;
import pack.entity.Follow;
import pack.entity.Post;
import pack.entity.PostLike;
import pack.entity.Product;
import pack.entity.ReportedPost;
import pack.entity.User;
import pack.repository.CommentLikeRepository;
import pack.repository.CommentsRepository;
import pack.repository.FollowsRepository;
import pack.repository.PostLikeRepository;
import pack.repository.PostsRepository;
import pack.repository.ReportedPostsRepository;
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

	@Autowired
	private ReportedPostsRepository rprps;

	// 유저 정보 하나 가져오기
	public UserDto userInfo(int no) {
		return User.toDto(urps.findById(no).get());
	}

	// 유저 정보 수정하기
	@Transactional
	public boolean userInfoUpdate(int userNo, UserDto dto) {
		try {
			User user = urps.findById(userNo).get();

			if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
				user.setNickname(dto.getNickname());
			}
			user.setBio(dto.getBio());

			urps.save(user);
			return true;
		} catch (Exception e) {
			System.out.println("updatePosts ERROR : " + e.getMessage());
			return false;
		}
	}

	// 한 유저에 대한 팔로잉, 팔로워 정보 가져오기
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

	// 팔로잉 정보 가져오기
	public List<UserDto> followeeInfo(int no) {
		return frps.findByFollowerNo(no).stream().map(Follow::getFollowee).map(User::toDto)
				.collect(Collectors.toList());
	}

	// 팔로워 정보 가져오기
	public List<UserDto> followerInfo(int no) {
		return frps.findByFolloweeNo(no).stream().map(Follow::getFollower).map(User::toDto)
				.collect(Collectors.toList());
	}

	// 팔로우 여부 체크하기
	public boolean followCheck(int no, int fno) {
		return frps.findByFollowerNoAndFolloweeNo(no, fno).size() > 0 ? true : false;
	}

	// 팔로우 취소하기
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

	// 팔로우하기
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

	// 팔로잉 글 모아보기
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

	// 특정 유저 작성 글 보기
	public List<PostDto> postListByUser(int no) {
		return prps.findByUserNo(no).stream().map(Post::toDto).collect(Collectors.toList());
	}

	// 게시글 세부 보기
	public PostDetailDto postDetail(int postNo, Pageable pageable) {

		PostDto postInfo = Post.toDto(prps.findById(postNo).get());
		UserDto userInfo = User.toDto(urps.findById(postInfo.getUserNo()).get());

		Page<Comment> commentsPage = crps.findByPostNo(postNo, pageable);

        return PostDetailDto.builder()
                .posts(postInfo)
                .userPic(userInfo.getPic())
                .userNickname(userInfo.getNickname())
                .userBio(userInfo.getBio())
                .comments(commentsPage.getContent().stream()
                            .map(Comment::toDto)
                            .collect(Collectors.toList()))
                .totalPages(commentsPage.getTotalPages()) // 전체 페이지 수
                .currentPage(commentsPage.getNumber()) // 현재 페이지 번호
                .totalElements(commentsPage.getTotalElements()) // 총 댓글 수
                .build();
	}

	// 게시글 좋아요 취소하기
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

	// 게시글 좋아요
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

	// 댓글 좋아요 취소하기
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

	// 댓글 좋아요
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

	// 게시글 좋아요 수 가져오기
	public List<PostLikeDto> getPostLike(int no) {
		return plrps.findByPostNo(no).stream().map(PostLike::toDto).collect(Collectors.toList());
	}

	// 댓글 좋아요 수 가져오기
	public List<CommentLikeDto> getCommentLike(int no) {
		return clrps.findByCommentNo(no).stream().map(CommentLike::toDto).collect(Collectors.toList());
	}

	// 게시글 좋아요 여부 확인하기
	public boolean checkPostLike(int postNo, int userNo) {
		return plrps.findByPostNoAndUserNo(postNo, userNo).size() == 0 ? false : true;
	}

	// 댓글 좋아요 여부 확인하기
	public boolean checkCommentLike(int commentNo, int userNo) {
		return clrps.findByCommentNoAndUserNo(commentNo, userNo).size() == 0 ? false : true;
	}

	// 게시글 삭제하기
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

	// 게시글 등록하기
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

	// 게시글 수정하기
	@Transactional
	public boolean updatePosts(int postNo, PostDto dto) {
		try {
			Post post = prps.findById(postNo).get();

			if (dto.getContent() != null && !dto.getContent().isEmpty()) {
				post.setContent(dto.getContent());
			}
			post.setProduct(Product.builder().no(dto.getProductNo()).build());

			prps.save(post);
			return true;
		} catch (Exception e) {
			System.out.println("updatePosts ERROR : " + e.getMessage());
			return false;
		}
	}

	// 댓글 삭제하기
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

	// 댓글 등록하기
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

	// 게시글 신고하기
	@Transactional
	public boolean reportedPost(ReportedPostDto dto) {
		try {
			rprps.save(ReportedPostDto.toEntity(dto));
			return true;
		} catch (Exception e) {
			System.out.println("reportedPost ERROR : " + e.getMessage());
			return false;
		}
	}

	// 게시글 여러 개 삭제하기 (오버라이딩)
	@Transactional
	public boolean deletePosts(List<Integer> postIds) {
		boolean b = false;
		try {
			prps.deleteAllById(postIds);
			b = true;
		} catch (Exception e) {
			System.out.println("deletePosts ERROR : " + e.getMessage());
		}
		return b;
	}

}
