package com.openclassrooms.javaspring.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.javaspring.repository.UserRepository;
import com.openclassrooms.javaspring.model.User;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }


}
