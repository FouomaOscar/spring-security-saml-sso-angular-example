package com.easyup.samldemo.services;

import com.easyup.samldemo.models.AuthDto;
import com.easyup.samldemo.models.UserDto;

public interface UserService {

    UserDto authenticate(AuthDto authDto);
    UserDto getAuthUser(String token);
}
