package com.coupon.mapper;

import com.coupon.data.dto.CouponDto;
import com.coupon.data.entity.Coupon;

public interface CouponMapper {
    Coupon toEntity(CouponDto dto);
    CouponDto toDto(Coupon entity);
}
