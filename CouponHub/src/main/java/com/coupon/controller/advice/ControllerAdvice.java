package com.coupon.controller.advice;

import com.coupon.service.ex.CouponExpiredException;
import com.coupon.service.ex.NoCouponLeftException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ControllerAdvice.class)
@ResponseBody
public class ControllerAdvice {

    @ExceptionHandler({CouponExpiredException.class,  NoCouponLeftException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleCouponException(RuntimeException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
