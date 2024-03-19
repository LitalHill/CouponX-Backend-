package com.coupon.service.ex;

import com.coupon.data.entity.Coupon;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException(String massage) {
        super(massage);
    }
}
