package com.core.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.sharedDTO.shared.dto.RequestDTO;

@Service
public class EmailConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumerService.class);
    private final Map<String, List<RequestDTO>> messageHistory = new ConcurrentHashMap<>();

    

    @KafkaListener(topics = "${core.request.message.topic}")
    public void consume(RequestDTO message) {
        logger.info("Processing new message: {}", message.getMessage());
        System.out.println(message.getSender());
        System.out.println(message.getMessage());

        // Add your business logic here
        processMessage(message);
        receiveMessage(message);
    }

    private void processMessage(RequestDTO message) {
        logger.info("Message processed successfully");
    }

    public void receiveMessage(RequestDTO message) {
        messageHistory.computeIfAbsent(message.getSender(), k -> new ArrayList<>())
                .add(message);
    }

    public void responseMessage(String username, RequestDTO message) {
        messageHistory.computeIfAbsent(username, k -> new ArrayList<>())
                .add(message);
    }

    public Map<String, List<RequestDTO>> getAllMessages() {
        return messageHistory.isEmpty() ? new ConcurrentHashMap<>() : messageHistory;
    }

    public List<RequestDTO> getMessageByUser(String username) {
        return messageHistory.getOrDefault(username, new ArrayList<>());
    }


}