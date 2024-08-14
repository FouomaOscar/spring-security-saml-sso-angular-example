package com.easyup.samldemo.services;

import com.easyup.samldemo.entities.RefreshToken;
import com.easyup.samldemo.models.TokenRefreshRequest;
import com.easyup.samldemo.models.TokenRefreshResponse;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Long userId);

    TokenRefreshResponse refreshtoken(TokenRefreshRequest refreshRequest);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);
}
