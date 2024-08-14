package com.easyup.samldemo.models;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String authToken;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String refreshToken;

    @Valid
    private List<String> roles;
}
