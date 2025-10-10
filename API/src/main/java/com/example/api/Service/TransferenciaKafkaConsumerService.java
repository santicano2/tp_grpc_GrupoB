package com.example.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaKafkaConsumerService {
    private final List<String> transferenciasRecibidas = new ArrayList<>();

    @KafkaListener(topics = "transferencia-donaciones-#{T(java.lang.System).getProperty('idOrgSolicitante','123')}", groupId = "api-transfer-consumer-group")
    public void escucharTransferencia(ConsumerRecord<String, String> record) {
        // Guarda el mensaje recibido en memoria (puedes parsear el JSON si lo deseas)
        transferenciasRecibidas.add(record.value());
        System.out.println("Transferencia recibida: " + record.value());
    }

    public List<String> getTransferenciasRecibidas() {
        return transferenciasRecibidas;
    }
}
