package com.example.api.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.api.dto.SolicitudDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SolicitudKafkaService {
    private final List<SolicitudDTO> solicitudesExternas = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "solicitud-donaciones", groupId = "solicitudes-group")
    public void listen(String message) {
        try {
            SolicitudDTO dto = objectMapper.readValue(message, SolicitudDTO.class);
            solicitudesExternas.add(dto);
        } catch (JsonProcessingException e) {
            // Loguear el error y continuar
            System.err.println("Error al parsear mensaje de Kafka: " + e.getMessage());
        }
    }

    public List<SolicitudDTO> getSolicitudesExternas() {
        return solicitudesExternas;
    }
}
