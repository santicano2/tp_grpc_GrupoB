package com.example.api.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.api.dto.AdhesionEventoDTO;
import com.example.api.dto.BajaEventoDTO;
import com.example.api.dto.EventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventoKafkaConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<EventDTO> eventosExternos = new CopyOnWriteArrayList<>();
    private final List<String> bajasProcesadas = new CopyOnWriteArrayList<>();
    private final List<AdhesionEventoDTO> adhesionesRecibidas = new CopyOnWriteArrayList<>();

    // Recibe eventos externos (EventDTO)
    @KafkaListener(topics = "eventos-solidarios", groupId = "eventos-group")
    public void recibirEvento(ConsumerRecord<String, String> record) {
        try {
            EventDTO evento = objectMapper.readValue(record.value(), EventDTO.class);

            // DESCARTAR eventos propios
            if ("1".equals(evento.getCreatedBy())) return; // reemplazar "1" por tu orgId real

            // GUARDAR evento si no está dado de baja
            if (!bajasProcesadas.contains(String.valueOf(evento.getId()))) {
                eventosExternos.add(evento);
                System.out.println("Evento externo recibido: " + evento.getName());
            }

        } catch (Exception e) {
            System.err.println("Error parseando evento: " + e.getMessage());
        }
    }

    // Recibe bajas de eventos (BajaEventoDTO)
    @KafkaListener(topics = "baja-evento-solidario", groupId = "eventos-group")
    public void recibirBaja(ConsumerRecord<String, String> record) {
        try {
            BajaEventoDTO bajaEvento = objectMapper.readValue(record.value(), BajaEventoDTO.class);
            String key = String.valueOf(bajaEvento.getIdEvento());
            bajasProcesadas.add(key);
            System.out.println("Evento dado de baja: " + key);
        } catch (Exception e) {
            System.err.println("Error parseando bajaEvento: " + e.getMessage());
        }
    }

    // Recibe adhesiones a eventos (AdhesionEventoDTO)
    @KafkaListener(topicPattern = "adhesion-evento-.*", groupId = "eventos-group")
    public void recibirAdhesion(ConsumerRecord<String, String> record) {
        try {
            AdhesionEventoDTO adhesion = objectMapper.readValue(record.value(), AdhesionEventoDTO.class);
            adhesionesRecibidas.add(adhesion);
            System.out.println("Adhesión recibida de " + adhesion.getNombre() + " " + adhesion.getApellido() + " al evento " + adhesion.getIdEvento());
        } catch (Exception e) {
            System.err.println("Error parseando adhesión: " + e.getMessage());
        }
    }

    // Getters
    public List<EventDTO> getEventosExternos() { return eventosExternos; }
    public List<String> getBajasProcesadas() { return bajasProcesadas; }
    public List<AdhesionEventoDTO> getAdhesiones() { return adhesionesRecibidas; }
}