package com.kafka_services.kafka_services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kafka_services.kafka_services.dto.RequestDTO;

@Controller
public class ChatController {
@Autowired
private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topicos.pagamento.request.topic}")
    private String emailRequestTopic;

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody RequestDTO message) {
        try {
            kafkaTemplate.send(emailRequestTopic, message);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }
    
}
