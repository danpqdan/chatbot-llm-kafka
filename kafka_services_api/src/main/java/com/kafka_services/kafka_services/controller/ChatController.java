package com.kafka_services.kafka_services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kafka_services.kafka_services.dto.RequestDTO;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${request.message.topic}")
    private String messageRequestTopic;

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody RequestDTO message) {
        try {
            // Send to Kafka
            kafkaTemplate.send(messageRequestTopic, message);
            
            // Broadcast message to WebSocket subscribers
            messagingTemplate.convertAndSend("/topic/messages", message);
            
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }

    @MessageMapping("/")
    @SendTo("/topic/messages")
    public RequestDTO handleWebSocketMessage(RequestDTO message) {
        return message;
    }
}
