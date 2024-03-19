package com.coupon.service;

import com.coupon.data.dto.CouponDto;

import java.util.Set;
import java.util.UUID;

public interface CouponService {
    CouponDto getCouponByUuid(UUID uuid);
    Set<CouponDto> getAllCustomerCoupons(String token);
    Set<CouponDto> getAllCompanyCoupons(String token);
    CouponDto purchaseCoupon(String token, UUID couponUuid);
    Set<CouponDto> getAllCoupons();

    Set<CouponDto> searchCouponByCategory(String title);
}
