package com.app.expd.service;

import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.RefreshToken;
import com.app.expd.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenServie {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String refreshToken){

        refreshTokenRepository.findByRefreshToken(refreshToken).
                orElseThrow(() -> new LetsTalkException("Invalid Refresh token sent"));
    }

    public void deleteRefreshToken(String refreshToken){

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
