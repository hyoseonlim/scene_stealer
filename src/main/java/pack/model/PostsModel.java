package pack.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

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
import pack.entity.Order;
import pack.entity.Post;
import pack.entity.PostLike;
import pack.entity.Product;
import pack.entity.User;
import pack.repository.AdminsRepository;
import pack.repository.CommentLikeRepository;
import pack.repository.CommentsRepository;
import pack.repository.FollowsRepository;
import pack.repository.OrderProductRepository;
import pack.repository.OrdersRepository;
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

	@Autowired
	private AdminsRepository arps;
	
	@Autowired
	private OrdersRepository orps;
	
	@Autowired
	private OrderProductRepository oprps;

	// 유저 정보 하나 가져오기
	public UserDto userInfo(int no) {
		return User.toDto(urps.findById(no).get());
	}

	// 유저 정보 수정하기
	@Transactional
	public boolean userInfoUpdate(int userNo, UserDto dto) {
		try {
			User user = urps.findById(userNo).orElseThrow(() -> new RuntimeException("User not found"));

			if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
				user.setNickname(dto.getNickname());
			}
			user.setBio(dto.getBio());

			// 프로필 이미지 처리
			if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
				String imageUrl = saveProfileImage(dto.getProfileImage());
				user.setPic(imageUrl);
			} else if (user.getPic() == null || user.getPic().isEmpty()) {
				// 이미지가 업로드되지 않았고, 사용자가 기존 이미지가 없는 경우 기본 이미지 설정
				user.setPic("/images/default.png"); // 기본 이미지 경로
			}

			urps.save(user);
			return true;
		} catch (Exception e) {
			System.out.println("updatePosts ERROR : " + e.getMessage());
			return false;
		}
	}

	private String saveProfileImage(MultipartFile profileImage) {
		try {
			UUID uuid = UUID.randomUUID();
			String imageFileName = uuid + "_" + profileImage.getOriginalFilename();
			Path destinationFilePath = Paths.get("src/main/resources/static/images", imageFileName);
			Files.copy(profileImage.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

			return "/images/" + imageFileName;
		} catch (IOException e) {
			throw new RuntimeException("Failed to store profile image", e);
		}
	}

	// 한 유저에 대한 팔로잉, 팔로워 정보 가져오기
	public Map<String, List<Integer>> followInfo(int no) {
		List<Integer> followerList = frps.findByFolloweeNoAndFollowerEmailIsNotNull(no).stream().map(f -> f.getFollower().getNo())
				.collect(Collectors.toList());
		List<Integer> followeeList = frps.findByFollowerNoAndFolloweeEmailIsNotNull(no).stream().map(f -> f.getFollowee().getNo())
				.collect(Collectors.toList());
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		result.put("followerList", followerList);
		result.put("followeeList", followeeList);
		return result;
	}

	// 팔로잉 정보 가져오기
	public Page<UserDto> followeeInfo(int no, Pageable pageable) {
		List<Integer> followList = frps.findByFollowerNoAndFolloweeEmailIsNotNull(no).stream().map((res) -> res.getFollowee().getNo())
				.collect(Collectors.toList());
		Page<User> followPage = urps.findByNoIn(followList, pageable);
		List<UserDto> followDtoList = followPage.stream().map(User::toDto).collect(Collectors.toList());
		return new PageImpl<>(followDtoList, pageable, followPage.getTotalElements());

	}

	// 팔로워 정보 가져오기
	public Page<UserDto> followerInfo(int no, Pageable pageable) {

		List<Integer> followList = frps.findByFolloweeNoAndFollowerEmailIsNotNull(no).stream().map((res) -> res.getFollower().getNo())
				.collect(Collectors.toList());
		Page<User> followPage = urps.findByNoIn(followList, pageable);
		List<UserDto> followDtoList = followPage.stream().map(User::toDto).collect(Collectors.toList());
		return new PageImpl<>(followDtoList, pageable, followPage.getTotalElements());
	}

	// 팔로우 여부 체크하기
	public boolean followCheck(int no, int fno) {
		return frps.findByFollowerNoAndFolloweeNo(no, fno).size() > 0 ? true : false;
	}

	// 신고 여부 체크하기
	public boolean reportCheck(int userNo, int postNo) {
		return rprps.findByUserNoAndPostNo(userNo, postNo).size() > 0 ? true : false;
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
	
	// 팔로우한 사람 글 모아보기 또는 좋아요 순으로 게시글 가져오기 메소드 수정
	// 팔로우한 사람 글 모아보기 또는 좋아요 순으로 게시글 가져오기 메소드 수정
	public Page<PostDto> followPostListOrPopular(int userNo, Pageable pageable) {
	    // 팔로우한 사용자의 번호를 가져옴
	    List<Integer> followeeList = frps.findByFollowerNoAndFolloweeEmailIsNotNull(userNo).stream()
	        .map(f -> f.getFollowee().getNo())
	        .collect(Collectors.toList());

	    Page<Post> postPage;

	    if (followeeList.isEmpty()) {
	        // 팔로우한 사용자가 없으면 좋아요 순으로 삭제되지 않고 신고 횟수가 5회 이하인 게시글을 가져옴
	        Pageable sortedByLikes = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	            Sort.by(Sort.Direction.DESC, "likesCount"));
	        postPage = prps.findByDeletedFalseAndReportsCountLessThanEqual(5, sortedByLikes); // 수정된 부분
	    } else {
	        // 팔로우한 사용자가 있으면 해당 사용자의 삭제되지 않고 신고 횟수가 5회 이하인 게시글을 가져옴
	        postPage = prps.findByUserNoInAndDeletedFalseAndReportsCountLessThanEqual(followeeList, 5, pageable); // 수정된 부분
	    }

	    return postPage.map(Post::toDto);
	}


	// 특정 유저 작성 글 보기
	public Page<PostDto> postListByUser(int userNo, Pageable pageable) {
		Page<Post> postPage = prps.findByUserNo(userNo, pageable);
		return postPage.map(Post::toDto);
	}

	// 게시글 세부 보기
	public PostDetailDto postDetail(int postNo, Pageable pageable) {
		
		PostDto postInfo = Post.toDto(prps.findById(postNo).get());
		UserDto userInfo = User.toDto(urps.findById(postInfo.getUserNo()).get());

		// 부모 댓글을 페이징으로 가져옴
		Page<Comment> parentCommentsPage = crps.findByPostNoAndParentCommentNoIsNullOrderByNoDesc(postNo, pageable);

		// 각 부모 댓글에 대해 자식 댓글을 함께 가져옴
		List<CommentDto> commentsWithReplies = parentCommentsPage.getContent().stream().map(parentComment -> {
			List<CommentDto> replies = crps.findByParentCommentNo(parentComment.getNo()).stream().map(Comment::toDto)
					.collect(Collectors.toList());
			CommentDto parentCommentDto = Comment.toDto(parentComment);
			parentCommentDto.setReplies(replies); // 부모 댓글에 자식 댓글(답글) 추가
			return parentCommentDto;
		}).collect(Collectors.toList());

		return PostDetailDto.builder().posts(postInfo).userPic(userInfo.getPic()).userNickname(userInfo.getNickname())
				.userBio(userInfo.getBio()).userId(userInfo.getId()).comments(commentsWithReplies)
				.totalPages(parentCommentsPage.getTotalPages()) // 전체 페이지 수
				.currentPage(parentCommentsPage.getNumber()) // 현재 페이지 번호
				.totalElements(parentCommentsPage.getTotalElements()) // 총 댓글 수
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
			Post post = prps.findById(dto.getPostNo()).get();
			post.setReportsCount(post.getReportsCount() + 1);
			prps.save(post);
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

	// 소프트 삭제메서드
	@Transactional
	public boolean softDeletePost(int postNo) {
		try {
			Post post = prps.findById(postNo).orElseThrow(() -> new RuntimeException("Post not found"));
			post.setDeleted(true);
			post.setDeletedAt(new java.util.Date());
			prps.save(post);
			return true;
		} catch (Exception e) {
			System.out.println("softDeletePost ERROR : " + e.getMessage());
			return false;
		}
	}

	// 복구 메소드
	@Transactional
	public boolean restorePost(int postNo) {
		try {
			Post post = prps.findById(postNo).orElseThrow(() -> new RuntimeException("Post not found"));
			if (post.isDeleted()) {
				post.setDeleted(false);
				post.setDeletedAt(null);
				prps.save(post);
				return true;
			}
			return false; // 이미 활성화된 게시물인 경우
		} catch (Exception e) {
			System.out.println("restorePost ERROR : " + e.getMessage());
			return false;
		}
	}

	// 완전 삭제 메소드
	@Transactional
	public boolean permanentDeletePost(int postNo) {
		try {
			int rowsDeleted = prps.deleteByNoAndDeletedTrue(postNo);
			return rowsDeleted > 0;
		} catch (Exception e) {
			System.out.println("permanentDeletePost ERROR : " + e.getMessage());
			return false;
		}
	}

	public Page<PostDto> postListByUser1(int userNo, Pageable pageable) {
		Page<Post> postPage = prps.findActiveByUserNo(userNo, pageable); // 수정된 부분
		return postPage.map(Post::toDto);
	}

	// 휴지통에서 삭제된 게시물조회
	public Page<PostDto> getDeletedPosts(Pageable pageable) {
		Page<Post> postPage = prps.findDeletedPosts(pageable);
		return postPage.map(Post::toDto);
	}

	// 유저별 휴지통에서 삭제된 게시물조회
	public Page<PostDto> getDeletedPostsByUser(int userNo, Pageable pageable) {
		Page<Post> postPage = prps.findDeletedPostsByUserNo(userNo, pageable);
		return postPage.map(Post::toDto);
	}

	
	// 인기 게시글 가져오기 메소드 수정
	public Page<PostDto> getPopularPosts(Pageable pageable) {
	    Pageable sortedByLikes = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
	        Sort.by(Sort.Direction.DESC, "likesCount"));
	    // 좋아요 수 기준으로 정렬된 삭제되지 않고 신고 횟수가 5회 이하인 게시물만 가져옴
	    Page<Post> postPage = prps.findByDeletedFalseAndReportsCountLessThanEqual(5, sortedByLikes); // 수정된 부분
	    return postPage.map(Post::toDto);
	}

	
	// 주문 상품 불러오기
	public List<ProductDto> getOrderProductList(int userNo) {
		List<Integer> orderNoList = orps.findTop5ByUserNoOrderByNoDesc(userNo).stream().map(Order::getNo).collect(Collectors.toList());
		
		List<Product> productList = oprps.findByOrderNoIn(orderNoList).stream().map(op -> op.getProduct()).collect(Collectors.toList());
		return productList.stream().map(Product::toDto).collect(Collectors.toList());
	}
	
	public boolean userInfoCheck(String id, int userNo) {
		boolean b = false;
		Optional<User> dto = urps.findByIdKAndNo(id, userNo);
		if(dto.isPresent()) {
			b = true;
		}
		return b;
	}

}
