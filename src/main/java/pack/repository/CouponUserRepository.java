package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CouponUser;

public interface CouponUserRepository extends JpaRepository<CouponUser, Integer> {

}
