package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.AuthSuccess;
import com.openclassrooms.javaspring.dto.LoginRequest;
import com.openclassrooms.javaspring.dto.RegisterRequest;
import com.openclassrooms.javaspring.dto.UserResponse;
import com.openclassrooms.javaspring.model.User;
/*import com.openclassrooms.javaspring.service.JWTService;*/
import com.openclassrooms.javaspring.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

   /* private JWTService jwtService;

    public UserController(JWTService jwtService) {
        this.jwtService = jwtService;
    }*/

   /* public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return token;
    }*/


/*    @GetMapping("/test/{id}")
    public ResponseEntity<User> getUserTest(@PathVariable("id") final String string_id) {
    	long id = Long.parseLong(string_id);
        Optional<User> user = userService.getUserById2(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
*/
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
    public User getUser(@RequestBody UserResponse userDto) {
        User user = new User();

        return user;
    }
    @PostMapping("/auth/login")
    public AuthSuccess login(@RequestBody LoginRequest loginRequest){
        AuthSuccess authSuccess = new AuthSuccess();
        //getToken(authentication)
        return authSuccess;
    }

    @PostMapping("/auth/register")
    public AuthSuccess register(@RequestBody RegisterRequest registerRequest){
        AuthSuccess authSuccess = new AuthSuccess();
        return authSuccess;
    }

}
