package pack.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.UserDto;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    private String id;
    
    @Column(name="id_k")
    private String idK;
    
    @Column(name="id_n")
    private String idN;
    
    @Column(name="id_g")
    private String idG;
    
    private String pwd;
    private String name;
    private String tel;
    private String email;
    private String zipcode;
    private String address;
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
    private List<CouponUser> couponUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CharacterLike> characterLikes = new ArrayList<>();
    
    @OneToMany(mappedBy = "followee", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followees = new ArrayList<>();
    


    public static UserDto toDto(User entity) {
        return UserDto.builder()
        		.no(entity.getNo())
                .id(entity.getId())
                .idG(entity.getIdG())
                .idK(entity.getIdK())
                .idN(entity.getIdN())
                .pwd(entity.getPwd())
                .name(entity.getName())
                .tel(entity.getTel())
                .email(entity.getEmail())
                .zipcode(entity.getZipcode())
                .address(entity.getAddress())
                .nickname(entity.getNickname())
                .bio(entity.getBio())
                .pic(entity.getPic())
                .postsNoList(entity.getPosts().stream().map(Post::getNo).collect(Collectors.toList()))
                .reviewsNoList(entity.getReviews().stream().map(Review::getNo).collect(Collectors.toList()))
                .alertsNoList(entity.getAlerts().stream().map(Alert::getNo).collect(Collectors.toList()))
                .couponNoList(entity.getCouponUsers().stream().map(CouponUser::getNo).collect(Collectors.toList()))
                .build();
    }
    
    public void updatePassword(String email, String encodedPassword) {
        this.pwd = encodedPassword;
        // Optionally, you might want to update the reset token and expiration fields here as well
    }
}

