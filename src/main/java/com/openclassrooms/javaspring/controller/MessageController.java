package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.MessageRequest;
import com.openclassrooms.javaspring.dto.MessageResponse;
import com.openclassrooms.javaspring.model.Message;
import com.openclassrooms.javaspring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public MessageResponse createMessage(@RequestBody MessageRequest messageRequest) {
        //créer une methode convert dans le dto
        Message message = new Message();
        message.setMessage(messageRequest.getMessage());
        //TODO mettre le user courant
        //TODO créer un service pour récup le rental
        message.setCreated_at(new Date());
        message.setUpdated_at(new Date());

        messageService.createMessage(message);

        MessageResponse response = new MessageResponse();
        response.setMessage("Message send with success");
        return response;
    }
}

