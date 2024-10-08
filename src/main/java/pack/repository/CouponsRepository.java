package pack.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Coupon;

public interface CouponsRepository extends JpaRepository<Coupon, Integer>{
	Page<Coupon> findByNoIn(List<Integer> couponNoList, Pageable pageable);
	
	 // 만료일이 현재 날짜 이후인 쿠폰을 찾는 메서드
    Page<Coupon> findByExpiryDateAfter(Date expiryDate, Pageable pageable);
 // 만료일자가 현재 날짜 이후이거나 만료일자가 없는 쿠폰을 조회하는 메서드

    
    Page<Coupon> findAll(Pageable pageable); // 기본적인 findAll 메서드
    Page<Coupon> findByExpiryDateAfterOrExpiryDateIsNull(Date date, Pageable pageable);
}
