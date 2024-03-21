package com.openclassrooms.javaspring.service;


import java.util.Date;
import java.util.Optional;

import com.openclassrooms.javaspring.dto.MessageRequest;
import com.openclassrooms.javaspring.model.Message;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.repository.MessageRepository;
import com.openclassrooms.javaspring.repository.RentalRepository;
import com.openclassrooms.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    public UserRepository userRepository;

    public void createMessage(MessageRequest messageRequest) throws Exception {
        //convertir messageRequest en message
        Message message = new Message();
        message.setMessage(messageRequest.getMessage());

        Optional<Rental> rentalOptional = rentalRepository.findById(messageRequest.getRental_id());
        if (rentalOptional.isEmpty()) {
            throw new Exception("Rental not found with ID: " + messageRequest.getRental_id());
        }
        Optional<User> userOptional = userRepository.findById(messageRequest.getUser_id());
        if (!userOptional.isPresent()) {
            throw new Exception("User not found with ID: " + messageRequest.getUser_id());
        }
        User user = userOptional.get();
        message.setUser(user);

        Rental rental = rentalOptional.get();
        message.setRental(rental);

        message.setCreated_at(new Date());
        message.setUpdated_at(new Date());

        messageRepository.save(message);
    }

}

