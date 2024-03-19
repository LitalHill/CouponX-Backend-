package com.coupon.data.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private String username;
    private UUID useUuid;

}
