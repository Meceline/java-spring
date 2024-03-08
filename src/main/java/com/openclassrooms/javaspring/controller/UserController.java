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

/*    @GetMapping("/test/{id}")
    public ResponseEntity<User> getUserTest(@PathVariable("id") final String string_id) {
    	long id = Long.parseLong(string_id);
        Optional<User> user = userService.getUserById2(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
*/
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") final String string_id) {
        long id = Long.parseLong(string_id);
        User user = userService.getUserById(id);
        return user;
    }

}
