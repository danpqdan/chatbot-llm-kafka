package com.kafka_services.kafka_services.config;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

@Configuration
public class KafkaProducerConfig {
    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${core.request.message.topic}")
    private String messageRequestTopic;

    @Bean
    ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", JsonSerializer.class);
        // Número de confirmações que o produtor requer do servidor
        properties.put("acks", "all");
        // Número de tentativas se o produtor falhar ao enviar
        properties.put("retries", 3);
        // Tamanho do buffer para registros aguardando envio
        properties.put("batch.size", 16384);
        // Quanto tempo esperar antes de enviar um lote
        properties.put("linger.ms", 1000);
        // Tamanho máximo da requisição em bytes
        properties.put("max.request.size", 1048576);
        // Tempo máximo de espera pela resposta do servidor
        properties.put("request.timeout.ms", 30000);
        // Tamanho da memória buffer para registros aguardando envio
        properties.put("buffer.memory", 33554432);
        // Tipo de compressão para lotes
        properties.put("compression.type", "snappy");
        
        return new DefaultKafkaProducerFactory<>(properties);
    }
    
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic messageRequesTopic() {
        return TopicBuilder.name(messageRequestTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}