package com.coupon.service;

import com.coupon.data.dto.CouponDto;
import com.coupon.data.dto.UserDto;
import com.coupon.data.entity.Coupon;
import com.coupon.data.repo.Repository;
import com.coupon.mapper.CouponMapper;
import com.coupon.service.ex.CouponExpiredException;
import com.coupon.service.ex.NoCouponLeftException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final Repository repository;
    private final RestTemplate restTemplate;
    private final CouponMapper mapper;

    @Override
    public CouponDto getCouponByUuid(UUID uuid) {
        return mapper.toDto(repository.findByUuid(uuid));
    }

    @Override
    public Set<CouponDto> getAllCustomerCoupons(String token) {

        UserDto user = responseEntity(token);

        UUID customerUUID = user.getUseUuid();

        Set<Coupon> customerCoupons = repository.findByCustomersContaining(customerUUID);

        return customerCoupons.stream().map(mapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<CouponDto> getAllCompanyCoupons(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                "http://auth-forge/parse-token",
                HttpMethod.GET,
                httpEntity,
                UserDto.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null){

            UserDto user = responseEntity.getBody();

            Set<Coupon> setOfCoupons = repository.findAllByCompanyUuid(user.getUseUuid());

            return setOfCoupons.stream().map(mapper::toDto).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public CouponDto purchaseCoupon(String token, UUID couponUuid) {
        Coupon coupon = repository.findByUuid(couponUuid);
        LocalDateTime now = LocalDateTime.now();
        Integer amount = 0;

        if (coupon.getEndDate().isBefore(now)){
            throw new CouponExpiredException("Coupon expired, Try a different one");
        }

        if (coupon.getAmount() <= amount){
            throw new NoCouponLeftException("Sorry, There are no coupons left");
        } else {

            UserDto user = responseEntity(token);
            Set<UUID> customers = coupon.getCustomers();
            customers.add(user.getUseUuid());
        }
        return mapper.toDto(repository.save(coupon));
    }

    @Override
    public Set<CouponDto> getAllCoupons() {
        List<Coupon> allCoupons = repository.findAll();
        return allCoupons.stream().map(mapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<CouponDto> searchCouponByCategory(String category) {
        Set<Coupon> coupons = repository.findCouponByCategoryStartingWith(category);
        return coupons.stream().map(mapper::toDto).collect(Collectors.toSet());
    }


    private UserDto responseEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                "http://auth-forge/auth/parse-token",
                HttpMethod.GET,
                httpEntity,
                UserDto.class);

        return Objects.requireNonNull(responseEntity.getBody());
    }
}
