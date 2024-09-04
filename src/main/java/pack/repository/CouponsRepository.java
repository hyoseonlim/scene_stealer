package pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Coupon;

public interface CouponsRepository extends JpaRepository<Coupon, Integer>{
	Page<Coupon> findByNoIn(List<Integer> couponNoList, Pageable pageable);
	Page<Coupon> findAll(Pageable pageable);
}
