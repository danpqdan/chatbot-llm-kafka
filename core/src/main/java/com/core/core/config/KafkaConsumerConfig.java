package com.core.core.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.sharedDTO.shared.dto.RequestDTO;

@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private KafkaProperties kafkaConsumerProperties;

    @Bean
    public KafkaTemplate<String, Object> kafkaConsumerTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = kafkaConsumerProperties.buildConsumerProperties();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RequestDTO.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, Object>
    // kafkaListenerContainerFactory(
    // KafkaTemplate<String, Object> kafkaTemplate) {

    // ConcurrentKafkaListenerContainerFactory<String, Object> factory = new
    // ConcurrentKafkaListenerContainerFactory<>();
    // factory.setConsumerFactory(consumerFactory());

    // // Configura um erro handler para enviar mensagens inválidas para um Dead
    // Letter Topic
    // factory.setCommonErrorHandler(new DefaultErrorHandler(
    // new DeadLetterPublishingRecoverer(kafkaTemplate),
    // new FixedBackOff(0L, 2)
    // ));

    // return factory;
    // }

}