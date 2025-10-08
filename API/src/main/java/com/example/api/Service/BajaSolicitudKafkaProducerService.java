package com.example.api.service;

import com.example.api.dto.BajaSolicitudDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BajaSolicitudKafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public BajaSolicitudKafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarBaja(BajaSolicitudDTO baja) {
        String topic = "baja-solicitud-donaciones";
        try {
            String json = objectMapper.writeValueAsString(baja);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando baja de solicitud", e);
        }
    }
}
