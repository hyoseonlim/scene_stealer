package pack.entity;

import jakarta.persistence.*;
import lombok.*;
import pack.dto.CouponDto;
import pack.dto.CouponUserDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_user")
public class CouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "coupon_no")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    private Boolean isUsed;

    public static CouponUserDto toDto(CouponUser entity) {
        return CouponUserDto.builder()
            .no(entity.getNo())
            .couponNo(entity.getCoupon().getNo())
            .userNo(entity.getUser().getNo())
            .isUsed(entity.getIsUsed())
            .build();
    }
}
