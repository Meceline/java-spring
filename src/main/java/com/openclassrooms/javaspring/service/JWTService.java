package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.dto.UserResponse;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


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
       /* String userEmail = user.getEmail();
        String userName = user.getName();
        Long createdAt = user.getCreated_at().getTime(); // date en timestamp Unix
        Long updatedAt = user.getUpdated_at().getTime();
        System.out.println(userId + " " + userEmail + " " + userName);*/

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("id", userId)
                /*.claim("email", userEmail)
                .claim("name", userName)
                .claim("created_at", createdAt)
                .claim("updated_at", updatedAt)*/
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        System.out.println(this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue() + " generate token jwt");

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public UserResponse getUser(Jwt principal){
        Jwt jwt = (Jwt) principal;
        System.out.println(principal + " principal jwt");
        // extraction des claims de l'utilisateur depuis le token JWT
        Long id = jwt.getClaim("id");
        System.out.println(id + " id jwt " + jwt.getClaim("id"));

        User u = userService.getUserById(id);
        System.out.println(u.getName() + " name jwt ");
       /* String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");
        Long createdAtTimestamp = jwt.getClaim("created_at");
        Long updatedAtTimestamp = jwt.getClaim("updated_at");

        // Convertir les timestamps en objets Date
        Date createdAt = new Date(createdAtTimestamp);
        Date updatedAt = new Date(updatedAtTimestamp);
*/
        // Création de UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setEmail(u.getEmail());
        userResponse.setName(u.getName());
        userResponse.setCreated_at(u.getCreated_at());
        userResponse.setUpdated_at(u.getUpdated_at());
System.out.println(userResponse.getEmail() + " get User jwt");
        return userResponse;
    }
}
