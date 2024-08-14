package com.easyup.samldemo.services.impl;

import com.easyup.samldemo.config.jwt.JwtUtils;
import com.easyup.samldemo.entities.RefreshToken;
import com.easyup.samldemo.entities.User;
import com.easyup.samldemo.models.TokenRefreshRequest;
import com.easyup.samldemo.models.TokenRefreshResponse;
import com.easyup.samldemo.repositories.RefreshTokenRepository;
import com.easyup.samldemo.repositories.UserRepository;
import com.easyup.samldemo.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${easyup.app.jwtRefreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;


    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
                                   JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserId(userId);

            RefreshToken refreshToken;
            if (refreshTokenOptional.isPresent()) {
                refreshToken = refreshTokenOptional.get();
            } else {
                refreshToken = new RefreshToken();
                refreshToken.setUser(user.get());
            }

            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            return refreshTokenRepository.save(refreshToken);
        }
        return null;
    }

    @Override
    public TokenRefreshResponse refreshtoken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(requestRefreshToken);

        if (refreshToken.isPresent()) {
            User user = refreshToken.get().getUser();

            String token = jwtUtils.generateJwtToken(user.getUsername());
            return new TokenRefreshResponse(token, requestRefreshToken);
        }

        return null;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request " + token.getToken());
        }

        return token;
    }

    @Override
    public int deleteByUserId(Long userId) {
        return userRepository.findById(userId).map(refreshTokenRepository::deleteByUser).orElse(-1);
    }
}
