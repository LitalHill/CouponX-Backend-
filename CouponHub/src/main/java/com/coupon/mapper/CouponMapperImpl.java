package com.coupon.mapper;

import com.coupon.data.dto.CouponDto;
import com.coupon.data.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapperImpl implements CouponMapper {
    @Override
    public Coupon toEntity(CouponDto dto) {
        if (dto == null) {
            return null;
        } else {
            return Coupon.builder()
                    .uuid(dto.getUuid())
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .category(dto.getCategory())
                    .amount(dto.getAmount())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .imageUrl(dto.getImageUrl())
                    .price(dto.getPrice())
                    .build();
        }
    }

    @Override
    public CouponDto toDto(Coupon entity) {
        if (entity == null) {
            return null;
        } else {
            return CouponDto.builder()
                    .uuid(entity.getUuid())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .category(entity.getCategory())
                    .amount(entity.getAmount())
                    .startDate(entity.getStartDate())
                    .endDate(entity.getEndDate())
                    .imageUrl(entity.getImageUrl())
                    .price(entity.getPrice())
                    .build();
        }
    }
}
