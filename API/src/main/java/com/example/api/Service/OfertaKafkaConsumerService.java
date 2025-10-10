package com.example.api.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.api.dto.OfertaDonacionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OfertaKafkaConsumerService {
    private final List<OfertaDonacionDTO> ofertasExternas = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "oferta-donaciones", groupId = "ofertas-group")
    public void listen(String message) {
        try {
            OfertaDonacionDTO dto = objectMapper.readValue(message, OfertaDonacionDTO.class);
            ofertasExternas.add(dto);
            System.out.println("Oferta externa recibida: " + dto.getIdOferta());
        } catch (Exception e) {
            System.err.println("Error al parsear mensaje de Kafka (oferta): " + e.getMessage());
        }
    }

    public List<OfertaDonacionDTO> getOfertasExternas() {
        return ofertasExternas;
    }
}
