package pack.entity;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.UserDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "pic")
    private String pic;  // URL or file path

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CharacterLike> characterLikes = new ArrayList<>();
    
    public static UserDto toDto (User entity) {
    	return UserDto.builder()
    			.id(entity.getId())
    			.pwd(entity.getPwd())
    			.name(entity.getName())
    			.tel(entity.getTel())
    			.email(entity.getEmail())
    			.zipcode(entity.getZipcode())
    			.address(entity.getAddress())
    			.reward(entity.getReward())
    			.nickname(entity.getNickname())
    			.bio(entity.getBio())
    			.pic(entity.getPic())
    			.posts(entity.getPosts().stream().map(Post::toDto).collect(Collectors.toList()))
    			.comments(entity.getComments().stream().map(Comment::toDto).collect(Collectors.toList()))
    			.reviews(entity.getReviews().stream().map(Review::toDto).collect(Collectors.toList()))
    			.coupons(entity.getCoupons().stream().map(Coupon::toDto).collect(Collectors.toList()))
    			.alerts(entity.getAlerts().stream().map(Alert::toDto).collect(Collectors.toList()))
    			.characterLikes(entity.getCharacterLikes().stream().map(CharacterLike::toDto).collect(Collectors.toList()))
    			.build();
    }
}

