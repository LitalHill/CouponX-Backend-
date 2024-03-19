package com.coupon.data.repo;

import com.coupon.data.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Coupon, UUID> {
    Coupon findByUuid(UUID uuid);
    Set<Coupon> findByCustomersContaining(UUID uuid);
    Set<Coupon> findAllByCompanyUuid(UUID uuid);
    Set<Coupon> findCouponByCategoryStartingWith(String category);

}
