package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.dto.UserResponse;
import com.openclassrooms.javaspring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class JWTService {

    private JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

@Autowired
private UserService userService;

    public String generateToken(Authentication authentication, User user) {
        Instant now = Instant.now();
        Long userId = user.getId();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("id", userId)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public UserResponse getUser(Jwt principal){
        Jwt jwt = (Jwt) principal;
        // extraction des claims de l'utilisateur depuis le token JWT
        Long id = jwt.getClaim("id");

        User u = userService.getUserById(id);

        // Création de UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setEmail(u.getEmail());
        userResponse.setName(u.getName());
        userResponse.setCreated_at(u.getCreated_at());
        userResponse.setUpdated_at(u.getUpdated_at());
        return userResponse;
    }
}
