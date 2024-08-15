package pack.entity;


import jakarta.persistence.*;
import lombok.*;
import pack.dto.CouponDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;

	private String name;
	private Integer discountRate;
	private java.sql.Date expiryDate;
	private Boolean isUsed;

	@ManyToOne
	@JoinColumn(name = "user_no")
	private User user;
	
	public static CouponDto toDto(Coupon entity) {
		return CouponDto.builder()
				.no(entity.getNo())
				.name(entity.getName())
				.discountRate(entity.getDiscountRate())
				.expiryDate(entity.getExpiryDate())
				.isUsed(entity.getIsUsed())
				.user(User.toDto(entity.getUser()))
				.build();
	}

}
