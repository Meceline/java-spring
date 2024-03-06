package com.openclassrooms.javaspring.service;


import java.util.Date;

import com.openclassrooms.javaspring.model.Message;
import com.openclassrooms.javaspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public void createMessage(Message message) {
        messageRepository.save(message);
    }

}

