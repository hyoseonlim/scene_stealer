package pack.dto;

import lombok.*;
import pack.entity.CouponUser;
import pack.entity.Coupon;
import pack.entity.User;
import pack.repository.CouponsRepository;
import pack.repository.UsersRepository;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponUserDto_a {
    private Integer no;
    private CouponDto coupon;
    private UserDto user;
    private Integer couponNo;
    private Integer userNo;
    private Boolean isUsed;

    public static CouponUser toEntity(CouponUserDto_a dto, CouponsRepository couponService, UsersRepository userService) {
        // couponNo와 userNo로 엔티티를 조회 (Optional에서 실제 엔티티를 추출)
        Coupon coupon = couponService.findById(dto.getCouponNo())
                                     .orElseThrow(() -> new IllegalArgumentException("Invalid coupon ID: " + dto.getCouponNo()));
        User user = userService.findById(dto.getUserNo())
                               .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + dto.getUserNo()));

        return CouponUser.builder()
            .no(dto.getNo())
            .coupon(coupon)  // 조회한 Coupon 엔티티
            .user(user)      // 조회한 User 엔티티
            .isUsed(dto.getIsUsed())
            .build();
    }
}
