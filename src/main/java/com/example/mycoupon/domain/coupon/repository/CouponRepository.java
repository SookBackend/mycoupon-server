package com.example.mycoupon.domain.coupon.repository;

import com.example.mycoupon.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findById(Long id);

    List<Coupon> findByIssuerId(Long issuerId);
}
