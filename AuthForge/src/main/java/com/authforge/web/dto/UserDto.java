package com.authforge.web.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
    private final String username;
    private final UUID uuid;
}
