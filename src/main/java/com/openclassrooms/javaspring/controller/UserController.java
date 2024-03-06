package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") final String string_id) {
    	System.out.println("ola");
    	long id = Long.parseLong(string_id);
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
