package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Coupon;

public interface CouponsRepository extends JpaRepository<Coupon, Integer>{
	
	// 가장 큰 번호를 가진 쿠폰 반환 (이후 Controller에서 getNo로 MaxNo 받기 위한 용도)
	Coupon findTopByOrderByNoDesc();
	
	Page<Coupon> findByNoIn(List<Integer> couponNoList, Pageable pageable);
}
