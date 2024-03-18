package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
public class JWTService {

    private JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }



    public String generateToken(Authentication authentication, User user) {
        Instant now = Instant.now();
        Long userId = user.getId();
        String userEmail = user.getEmail();
        String userName = user.getName();
        Long createdAt = user.getCreated_at().getTime(); // date en timestamp Unix
        Long updatedAt = user.getUpdated_at().getTime();
        System.out.println(userId + " " + userEmail + " " + userName);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("id", userId)
                .claim("email", userEmail)
                .claim("name", userName)
                .claim("created_at", createdAt)
                .claim("updated_at", updatedAt)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
