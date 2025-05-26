package org.my_tma_test_app.tmaz.security.service;

import lombok.RequiredArgsConstructor;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public String generateJwtToken(TelegramUser user) {

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(86400);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .claim("firstName", user.getFirstName())
                .claim("username", user.getUsername())
                .claim("languageCode", user.getLanguageCode())
                .claim("photoUrl", user.getPhotoUrl())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
