package com.coupon.service.ex;

public class NoCouponLeftException extends RuntimeException {
    public NoCouponLeftException(String massage) {
        super(massage);
    }
}
