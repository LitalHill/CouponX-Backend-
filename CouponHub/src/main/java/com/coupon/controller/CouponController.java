package com.coupon.controller;

import com.coupon.data.dto.CouponDto;
import com.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService service;

    @GetMapping("/{uuid}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable UUID uuid){
        return ResponseEntity.ok(service.getCouponByUuid(uuid));
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<CouponDto>> getCustomerCoupons(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.getAllCustomerCoupons(token));
    }
    @GetMapping("/company")
    public ResponseEntity<Set<CouponDto>> getCompanyCoupons(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.getAllCompanyCoupons(token));
    }

    @GetMapping("/all")
    public ResponseEntity<Set<CouponDto>> gatAllCoupons(){
        return ResponseEntity.ok(service.getAllCoupons());
    }

    @PostMapping("/purchase/{uuid}")
    public ResponseEntity<CouponDto> purchaseCoupon(@RequestHeader("Authorization") String token,
                                                    @PathVariable UUID uuid){
        return ResponseEntity.status(HttpStatus.OK).body(service.purchaseCoupon(token, uuid));
    }

    @GetMapping("/search")
    public ResponseEntity<Set<CouponDto>> searchCouponByCategory(@RequestParam("category") String category){
        return ResponseEntity.ok(service.searchCouponByCategory(category));
    }


}
