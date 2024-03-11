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

    public Optional<User> getUserById2(Long id){
        return userRepository.findById(id);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public User getUserByEmailAndPassword(String email, String password){return userRepository.findUserByEmailAndPassword(email, password);}

    public User getUserByEmailAndNameAndPassword(String email, String name, String password){return userRepository.findUserByEmailAndNameAndPassword(email, name, password);}
}
