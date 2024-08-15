package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Coupon;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
	private Integer no;
	private String name;
	private Integer discountRate;
	private java.sql.Date expiryDate;

	public static Coupon toEntity(CouponDto dto) {
		return Coupon.builder()
				.no(dto.getNo())
				.name(dto.getName())
				.discountRate(dto.getDiscountRate())
				.expiryDate(dto.getExpiryDate())
				.build();
	}
}