package pack.dto;

import java.util.ArrayList;
import java.util.List;


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

    private List<Post> posts = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private List<Review> reviews = new ArrayList<>();

    private List<Coupon> coupons = new ArrayList<>();

    private List<Alert> alerts = new ArrayList<>();

    private List<CharacterLike> characterLikes = new ArrayList<>();
    
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
    			.posts(dto.getPosts())
    			.comments(dto.getComments())
    			.reviews(dto.getReviews())
    			.coupons(dto.getCoupons())
    			.alerts(dto.getAlerts())
    			.characterLikes(dto.getCharacterLikes())
    			.build();
    }

}
