package pack.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Alert;
import pack.entity.CharacterLike;
import pack.entity.Comment;
import pack.entity.Coupon;
import pack.entity.Post;
import pack.entity.Review;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer no;

    private String id;
    private String pwd;
    private String name;
    private String tel;
    private String email;
    private String zipcode;
    private String address;
    private Integer reward;
    private String nickname;
    private String bio;

    private String pic;  // URL or file path

    private List<PostDto> posts = new ArrayList<>();

    private List<CommentDto> comments = new ArrayList<>();

    private List<ReviewDto> reviews = new ArrayList<>();

    private List<CouponDto> coupons = new ArrayList<>();

    private List<AlertDto> alerts = new ArrayList<>();

    private List<CharacterLikeDto> characterLikes = new ArrayList<>();
    
    public static User toEntity(UserDto dto) {
    	return User.builder()
    			.id(dto.getId())
    			.pwd(dto.getPwd())
    			.name(dto.getName())
    			.tel(dto.getTel())
    			.email(dto.getEmail())
    			.zipcode(dto.getZipcode())
    			.address(dto.getAddress())
    			.reward(dto.getReward())
    			.nickname(dto.getNickname())
    			.bio(dto.getBio())
    			.pic(dto.getPic())
    			.posts(dto.getPosts().stream().map(PostDto::toEntity).collect(Collectors.toList()))
    			.comments(dto.getComments().stream().map(CommentDto::toEntity).collect(Collectors.toList()))
    			.reviews(dto.getReviews().stream().map(ReviewDto::toEntity).collect(Collectors.toList()))
    			.coupons(dto.getCoupons().stream().map(CouponDto::toEntity).collect(Collectors.toList()))
    			.alerts(dto.getAlerts().stream().map(AlertDto::toEntity).collect(Collectors.toList()))
    			.characterLikes(dto.getCharacterLikes().stream().map(CharacterLikeDto::toEntity).collect(Collectors.toList()))
    			.build();
    }

}
