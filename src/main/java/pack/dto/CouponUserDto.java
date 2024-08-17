package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Coupon;
import pack.entity.CouponUser;
import pack.entity.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponUserDto {
    private Integer no;
    private CouponDto coupon;
    private UserDto user;
    private Integer couponNo;
    private Integer userNo;
    private Boolean isUsed;

    public static CouponUser toEntity(CouponUserDto dto) {
        return CouponUser.builder()
            .no(dto.getNo())
            .coupon(CouponDto.toEntity(dto.getCoupon()))
            .user(UserDto.toEntity(dto.getUser()))
            .isUsed(dto.getIsUsed())
            .build();
    }
}
