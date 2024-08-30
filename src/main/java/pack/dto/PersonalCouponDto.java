package pack.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCouponDto {
	private CouponUserDto couponUser; // no, userNo, isUsed, couponNo
	private CouponDto coupon; // name, discountRate, expiryDate
}
