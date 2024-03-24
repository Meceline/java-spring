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
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") final String string_id) {
        try {
            long id = Long.parseLong(string_id);
            User u = userService.getUserById(id);
            if (u != null) {
                UserResponse userResponse = new UserResponse();
                userResponse.setId(u.getId());
                userResponse.setName(u.getName());
                userResponse.setEmail(u.getEmail());
                userResponse.setCreated_at(u.getCreated_at());
                userResponse.setUpdated_at(u.getUpdated_at());
                return ResponseEntity.ok(userResponse);
            } else {
                // id n'existe pas
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (NumberFormatException e) {
            // id n'est pas un nombre valide
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserResponse> getUser() {
        System.out.println("/me");
        try {
            // Obtenir l'objet Authentication du contexte de sécurité
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("/me" + authentication);
            // Authentification valide et utilisateur authentifié
            if (authentication != null && authentication.isAuthenticated()) {
                // Récupération de l'utilisateur à partir de l'objet Authentication
                Object principal = authentication.getPrincipal();
                System.out.println("/me" + principal);
                if (principal instanceof Jwt) {
                    UserResponse userResponse = jwtService.getUser((Jwt) principal);


                    System.out.println(userResponse.getName() + " / me");
                    return ResponseEntity.ok(userResponse);
                } else {
                    // Le principal n'est pas un objet Jwt
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }
            } else {
                // L'authentification est invalide ou l'utilisateur n'est pas authentifié
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        try{
            if (user != null) {
                // Construire un objet Authentication à partir de l'ID de l'utilisateur
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(),null );
                String token = jwtService.generateToken(authentication, user);
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            }else{
                return ResponseEntity.ok(Collections.singletonMap("error", "Authentication failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing your request"));
        }

    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest){
        try {
            User user = userService.register(registerRequest);
            System.out.println(user.getName() + " / register");
            if (user != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(),null );
                String token = jwtService.generateToken(authentication, user);
                System.out.println(token + " / register");
                return ResponseEntity.ok(Collections.singletonMap("token", token));

            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to register user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing your request"));
        }
    }

}
