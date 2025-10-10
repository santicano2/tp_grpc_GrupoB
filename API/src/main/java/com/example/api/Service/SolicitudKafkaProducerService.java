package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.api.dto.SolicitudDTO;

@Service
public class SolicitudKafkaProducerService {
    private static final String TOPIC = "solicitud-donaciones";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public void enviarSolicitud(SolicitudDTO solicitud) {
        try {
            String mensaje = objectMapper.writeValueAsString(solicitud);
            kafkaTemplate.send(TOPIC, mensaje);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar la solicitud", e);
        }
    }
}
