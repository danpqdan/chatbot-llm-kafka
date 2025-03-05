package com.core.core.service;

import com.core.core.dto.RequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailConsumerService.class);

    @KafkaListener(topics = "${request.message.topic}")
    public void consume(RequestDTO message) {
        logger.info("Processing new message: {}", message.message());
        System.out.println(message.sender());
        System.out.println(message.message());
        
        // Add your business logic here
        processMessage(message);
    }

    private void processMessage(RequestDTO message) {
        // Implement your message processing logic
        logger.info("Message processed successfully");
    }
}