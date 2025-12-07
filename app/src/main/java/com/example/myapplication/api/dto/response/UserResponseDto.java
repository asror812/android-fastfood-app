package com.example.myapplication.api.dto.response;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.myapplication.api.dto.request.UserDto;

public class UserResponseDto extends UserDto {
    private AddressResponseDto address;

    public Set<UUID> favouriteProducts;

    private List<UserBonusResponseDto> userBonuses;

    private Set<String> roles;
}
