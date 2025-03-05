package com.core.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.core.core.dto.RequestDTO;

@RestController
@CrossOrigin(origins = "*")
public class MessageController {
    
    private final Map<String, List<RequestDTO>> messageHistory = new ConcurrentHashMap<>();
    
    @KafkaListener(topics = "${request.message.topic}")
    public void receiveMessage(RequestDTO message) {
        messageHistory.computeIfAbsent(message.sender(), k -> new ArrayList<>())
                     .add(message);
    }
    
    @GetMapping("/messages")
    public ResponseEntity<Map<String, List<RequestDTO>>> getAllMessages() {
        return ResponseEntity.ok(messageHistory);
    }
    
    @PostMapping("/messages/{username}")
    public ResponseEntity<String> respondToUser(
            @PathVariable String username,
            @RequestBody RequestDTO response) {
        
        RequestDTO employeeResponse = new RequestDTO(
            response.message(),
            "Employee Support"
        );
        
        // Store the response
        messageHistory.computeIfAbsent(username, k -> new ArrayList<>())
                     .add(employeeResponse);
        
        return ResponseEntity.ok("Response sent successfully");
    }
    
    @GetMapping("/messages/{username}")
    public ResponseEntity<List<RequestDTO>> getMessagesByUsername(@PathVariable String username) {
        List<RequestDTO> messages = messageHistory.getOrDefault(username, new ArrayList<>());
        return ResponseEntity.ok(messages);
    }
}
