package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.MessageRequest;
import com.openclassrooms.javaspring.repository.UserRepository;
import com.openclassrooms.javaspring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageRequest messageRequest) throws Exception {
        try{
            messageService.createMessage(messageRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "Message send with success"));
        }catch (Exception e) {
            return handleServerError();
        }

    }

    private ResponseEntity<Map<String, String>> handleServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "An error occurred while processing your message request"));
    }
}

