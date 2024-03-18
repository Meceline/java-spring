package com.openclassrooms.javaspring.controller;


import com.openclassrooms.javaspring.dto.LoginRequest;
import com.openclassrooms.javaspring.dto.RegisterRequest;
import com.openclassrooms.javaspring.dto.UserResponse;
import com.openclassrooms.javaspring.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.openclassrooms.javaspring.service.JWTService;
import com.openclassrooms.javaspring.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Collections;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{id}")
    public UserResponse  getUserById(@PathVariable("id") final String string_id) {
        long id = Long.parseLong(string_id);
        User u = userService.getUserById(id);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(u.getId());
        userResponse.setName(u.getName());
        userResponse.setEmail(u.getEmail());
        userResponse.setCreated_at(u.getCreated_at());
        userResponse.setUpdated_at(u.getUpdated_at());
        return userResponse;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserResponse> getUser() {
        // Obtenir l'objet Authentication du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // authentification valide et utilisateur authentifié
        if (authentication != null && authentication.isAuthenticated()) {
            // Récupération de l'utilisateur à partir de l'objet Authentication
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;

                System.out.println(((Jwt) principal).getClaims());
                System.out.println(jwt);

                // extraction des claims de l'utilisateur depuis le token JWT
                Long id = jwt.getClaim("id");
                String email = jwt.getClaim("email");
                String name = jwt.getClaim("name");
                Long createdAtTimestamp = jwt.getClaim("created_at");
                Long updatedAtTimestamp = jwt.getClaim("updated_at");

                // Convertir les timestamps en objets Date
                Date createdAt = new Date(createdAtTimestamp);
                Date updatedAt = new Date(updatedAtTimestamp);


                // Création de UserResponse
                UserResponse user = new UserResponse();
                user.setId(id);
                user.setEmail(email);
                user.setName(name);
                user.setCreated_at(createdAt);
                user.setUpdated_at(updatedAt);
                // Retournez les informations sur l'utilisateur
                return ResponseEntity.ok(user);
            } else {
                // le principal n'est pas un objet Jwt
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            // l'authentification est invalide ou l'utilisateur n'est pas authentifié
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        System.out.println(user.getEmail());
        if (user != null) {
            // Construire un objet Authentication à partir de l'ID de l'utilisateur
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(),null );

            String token = jwtService.generateToken(authentication, user);
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        }else{
            return ResponseEntity.ok(Collections.singletonMap("error", "error"));
        }
    }



    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(Collections.singletonMap("error", "error"));
    }

}
