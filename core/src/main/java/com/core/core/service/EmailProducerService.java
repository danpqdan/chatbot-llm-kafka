package com.core.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sharedDTO.shared.dto.RequestDTO;

@Service
public class EmailProducerService {

    @Value("${cliente.request.message.topic}")
    private String clienteMessageRequestTopic;
    
    @Autowired
    EmailConsumerService emailConsumerService;
    @Autowired
    @Qualifier("producerKafkaTemplate")
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String username, String message) {
        RequestDTO employeeResponse = new RequestDTO(
                message,
                "Employee Support:"+username);
        kafkaTemplate.send(clienteMessageRequestTopic, employeeResponse);
        emailConsumerService.responseMessage(username, employeeResponse);
    }
    
}
