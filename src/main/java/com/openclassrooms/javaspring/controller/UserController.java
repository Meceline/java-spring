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

    private JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") final String string_id) {
        try {
            long id = Long.parseLong(string_id);
            User u = userService.getUserById(id);
            if (u != null) {
                return ResponseEntity.ok(convertUserToUserResponse(u));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return handleServerError();
        }
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getUser() {
        try {
            UserResponse userResponse = getUserFromAuthentication();
            if (userResponse != null) {
                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return handleServerError();
        }

    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest);
            if (user != null) {
                String token = generateToken(user);
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.ok(Collections.singletonMap("error", "Authentication failed"));
            }
        } catch (Exception e) {
            return handleServerError();
        }

    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest){
        try {
            User user = userService.register(registerRequest);
            if (user != null) {
                String token = generateToken(user);
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Failed to register user"));
            }
        } catch (Exception e) {
            return handleServerError();
        }
    }


    private UserResponse convertUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreated_at(user.getCreated_at());
        userResponse.setUpdated_at(user.getUpdated_at());
        return userResponse;
    }
    private String generateToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(), null);
        return jwtService.generateToken(authentication, user);
    }
    // récupére l'utilisateur à partir de l'objet Authentication
    private UserResponse getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt) {
            return jwtService.getUser((Jwt) authentication.getPrincipal());
        }
        return null;
    }
    private ResponseEntity<Map<String, String>> handleServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "An error occurred while processing your user request"));
    }

}
