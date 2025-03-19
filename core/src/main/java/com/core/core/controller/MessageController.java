package com.core.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharedDTO.shared.dto.RequestDTO;
import com.core.core.service.EmailConsumerService;
import com.core.core.service.EmailProducerService;

@RestController
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    EmailConsumerService emailConsumerService;
    @Autowired
    EmailProducerService emailProducerService;

    @GetMapping("/messages")
    public ResponseEntity<Map<String, List<RequestDTO>>> getAllMessages() {
        return ResponseEntity.ok(emailConsumerService.getAllMessages());
    }

    @PostMapping("/messages/{username}")
    public ResponseEntity<String> respondToUser(
            @PathVariable String username,
            @RequestBody RequestDTO response) {
        emailProducerService.sendMessage(username, response.getMessage());
        return ResponseEntity.ok("Response sent successfully");
    }

    @GetMapping("/messages/{username}")
    public ResponseEntity<List<RequestDTO>> getMessagesByUsername(@PathVariable String username) {
        return ResponseEntity.ok(emailConsumerService.getMessageByUser(username));
    }
}
