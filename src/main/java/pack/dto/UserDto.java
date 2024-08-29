package pack.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String pic;
    private MultipartFile profileImage;
    private String subpath;
    
    private List<CouponUserDto> couponUsers = new ArrayList<>();
    
    private List<Integer> couponNoList;
    private List<Integer> postsNoList;
    private List<Integer> reviewsNoList;
    private List<Integer> alertsNoList;
    
    // 페이징
    private int totalPages, currentPage; 
    private Long totalElements;

    
    public static User toEntity(UserDto dto) {
        return User.builder()
        		.no(dto.getNo())
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
                .subpath(dto.getSubpath())
                .couponUsers(dto.getCouponUsers().stream().map(CouponUserDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
