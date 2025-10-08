package com.example.api.service;

import com.example.api.dto.OfertaDonacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OfertaKafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public OfertaKafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarOferta(OfertaDonacionDTO oferta) {
        String topic = "oferta-donaciones";
        try {
            String json = objectMapper.writeValueAsString(oferta);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando oferta", e);
        }
    }
}
