package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.api.dto.TransferenciaDonacionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransferenciaKafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public TransferenciaKafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarTransferencia(Long idSolicitud, TransferenciaDonacionDTO transferencia) {
        String topic = "transferencia-donaciones-" + idSolicitud;
        try {
            String json = objectMapper.writeValueAsString(transferencia);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando transferencia", e);
        }
    }
}
