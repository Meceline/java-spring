package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.javaspring.repository.UserRepository;
import com.openclassrooms.javaspring.model.User;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

/*    public Optional<User> getUserById2(Long id){
        return userRepository.findById(id);
    }*/

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

      public User login(LoginRequest loginRequest) {
          String password = passwordEncoder.encode(loginRequest.getPassword());
          User user = userRepository.findUserByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
          return user;
      }

}
