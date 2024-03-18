package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.AuthSuccess;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

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
    public ResponseEntity<UserDetails> getUser() {
        // Obtenir l'objet Authentication du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Vérifier si l'authentification est valide et que l'utilisateur est authentifié
        if (authentication != null && authentication.isAuthenticated()) {
            // Récupérer l'utilisateur à partir de l'objet Authentication
            Object principal = authentication.getPrincipal();

            // Vérifier si le principal est un objet UserDetails
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                // Retourner les informations sur l'utilisateur
                return ResponseEntity.ok(userDetails);
            } else {
                // le principal n'est pas un objet UserDetails
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            // l'authentification est invalide ou que l'utilisateur n'est pas authentifié
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        if (user != null) {
            // Construire un objet Authentication à partir de l'ID de l'utilisateur
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(),null );

            String token = jwtService.generateToken(authentication);
            AuthSuccess authSuccess = new AuthSuccess("hjb");
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        }else{
            return ResponseEntity.ok(Collections.singletonMap("error", "error"));
        }
    }



    @PostMapping("/auth/register")
    public AuthSuccess register(@RequestBody RegisterRequest registerRequest){
        AuthSuccess authSuccess = new AuthSuccess("hjb");
        return authSuccess;
    }

}
