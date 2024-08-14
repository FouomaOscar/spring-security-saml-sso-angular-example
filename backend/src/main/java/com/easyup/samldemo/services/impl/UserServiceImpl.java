package com.easyup.samldemo.services.impl;

import com.easyup.samldemo.repositories.UserRepository;
import com.easyup.samldemo.config.jwt.JwtUtils;
import com.easyup.samldemo.entities.RefreshToken;
import com.easyup.samldemo.entities.Role;
import com.easyup.samldemo.entities.User;
import com.easyup.samldemo.models.AuthDto;
import com.easyup.samldemo.models.UserDto;
import com.easyup.samldemo.repositories.RoleRepository;
import com.easyup.samldemo.services.RefreshTokenService;
import com.easyup.samldemo.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.easyup.samldemo.entities.ERole.ROLE_USER;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;


    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                           RoleRepository roleRepository, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public UserDto authenticate(AuthDto authDto) {
        return login(registerOrGetUser(authDto));
    }

    @Override
    public UserDto getAuthUser(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer ","");
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            UserDto userDto = new UserDto();
            User user1 = user.get();
            userDto.setId(user1.getId());
            userDto.setUsername(user1.getUsername());
            userDto.setEmail(user1.getEmail());
            userDto.setFirstName(user1.getFirstName());
            return userDto;
        } else {
            return null;
        }
    }

    public UserDto login(UserDto loginRequest) {

        String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getId());

        UserDto userDto = new UserDto();
        userDto.setId(loginRequest.getId());
        userDto.setAuthToken(jwt);
        userDto.setEmail(loginRequest.getEmail());
        userDto.setUsername(loginRequest.getUsername());
        userDto.setFirstName(loginRequest.getFirstName());
        userDto.setLastName(loginRequest.getLastName());
        userDto.setRefreshToken(refreshToken.getToken());
        userDto.setRoles(loginRequest.getRoles());

        return userDto;
    }

    public UserDto registerOrGetUser(AuthDto authDto) {

        Optional<User> optionalUser = userRepository.findByUsername(authDto.getUsername());
        UserDto userDto = new UserDto();
        User user;
        Set<Role> roles = Set.of(new Role(1, ROLE_USER));

        if (optionalUser.isEmpty()) {
            user = new User(authDto.getUsername(), authDto.getUsername(),
                    authDto.getFirstName(),
                    null);

//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ROLE_USER)
//                    .orElseThrow(() -> responseStatusException);
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> responseStatusException);
//                        roles.add(adminRole);
//
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> responseStatusException);
//                        roles.add(modRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ROLE_USER)
//                                .orElseThrow(() -> responseStatusException);
//                        roles.add(userRole);
//                }
//            });
//        }
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRoles(roles.stream().map(role -> role.getName().name()).toList());

        return userDto;

    }
}
