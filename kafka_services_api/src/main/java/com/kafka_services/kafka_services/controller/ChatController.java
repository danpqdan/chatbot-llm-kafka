package com.kafka_services.kafka_services.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kafka_services.kafka_services.dto.RequestDTO;

@RestController
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${request.message.topic}")
    private String messageRequestTopic;

    private final Map<String, List<RequestDTO>> userMessages = new ConcurrentHashMap<>();

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody RequestDTO message) {
        try {
            userMessages.computeIfAbsent(message.sender(), k -> new ArrayList<>()).add(message);
            kafkaTemplate.send(messageRequestTopic, message);
            messagingTemplate.convertAndSend("/topic/messages", message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }

    @PostMapping("/messages/{username}")
    public ResponseEntity<String> receiveEmployeeResponse(@PathVariable String username, @RequestBody RequestDTO message) {
        try {
            // Create and store employee response
            RequestDTO employeeMessage = new RequestDTO("Employee Support", message.message());
            userMessages.computeIfAbsent(username, k -> new ArrayList<>()).add(employeeMessage);
            
            // Broadcast message through WebSocket
            messagingTemplate.convertAndSend("/topic/messages", employeeMessage);
            
            // Send to Kafka
            kafkaTemplate.send(messageRequestTopic, employeeMessage);
            
            return ResponseEntity.ok("Employee response processed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing response: " + e.getMessage());
        }
    }

    @GetMapping("/messages/{username}")
    public ResponseEntity<List<RequestDTO>> getUserMessages(@PathVariable String username) {
        List<RequestDTO> messages = userMessages.getOrDefault(username, new ArrayList<>());
        return ResponseEntity.ok(messages);
    }
}