package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.api.dto.AdhesionEventoDTO;
import com.example.api.dto.BajaEventoDTO;
import com.example.api.dto.PublicarEventoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventoKafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventoKafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // Publica un evento solidario (nuevo DTO)
    public void publicarEvento(PublicarEventoDTO evento) {
        try {
            String json = objectMapper.writeValueAsString(evento);
            kafkaTemplate.send("eventos-solidarios", json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando evento", e);
        }
    }

    // Publica un evento de baja
    public void bajaEvento(BajaEventoDTO bajaEvento) {
        try {
            String json = objectMapper.writeValueAsString(bajaEvento);
            kafkaTemplate.send("baja-evento-solidario", json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando bajaEvento", e);
        }
    }

    // Publica adhesión a evento
    public void adherirEvento(String idOrganizador, AdhesionEventoDTO adhesion) {
        try {
            String json = objectMapper.writeValueAsString(adhesion);
            kafkaTemplate.send("adhesion-evento-" + idOrganizador, json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando adhesión", e);
        }
    }
}
