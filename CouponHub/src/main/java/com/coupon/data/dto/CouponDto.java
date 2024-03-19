package com.coupon.data.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CouponDto {
    private UUID uuid;
    public String category;
    public String title;
    public String description;
    public LocalDateTime startDate, endDate;
    public BigDecimal price;
    public Integer amount;
    public URL imageUrl;
}
