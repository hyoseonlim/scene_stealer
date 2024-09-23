package pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CouponUser;

public interface CouponUserRepository extends JpaRepository<CouponUser, Integer> {
	
	public List<CouponUser> findByUserNo(int userNo);
	public List<CouponUser> findByUserNoAndIsUsedIsNull(int userNo);
	
	public Optional<CouponUser> findByCouponNoAndUserNo(int couponNo, int userNo);

}
