package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Coupon;

public interface CouponsRepository extends JpaRepository<Coupon, Integer>{

}
