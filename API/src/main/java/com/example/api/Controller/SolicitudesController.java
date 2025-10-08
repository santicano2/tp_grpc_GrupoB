package com.example.api.Controller;
import com.example.api.dto.BajaSolicitudDTO;
import com.example.api.service.BajaSolicitudKafkaProducerService;
import com.example.api.dto.OfertaDonacionDTO;
import com.example.api.service.OfertaKafkaProducerService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.api.dto.TransferenciaDonacionDTO;
import com.example.api.service.TransferenciaKafkaProducerService;
import com.example.api.service.TransferenciaKafkaConsumerService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    @Autowired
    private BajaSolicitudKafkaProducerService bajaSolicitudKafkaProducerService;

    // Endpoint para dar de baja una solicitud de donación
    @PostMapping("/baja")
    public org.springframework.http.ResponseEntity<String> bajaSolicitud(@RequestBody BajaSolicitudDTO baja) {
        if (baja == null || baja.getIdOrganizacionSolicitante() == null || baja.getIdSolicitud() == null) {
            return org.springframework.http.ResponseEntity.badRequest().body("Datos de baja incompletos");
        }
        bajaSolicitudKafkaProducerService.enviarBaja(baja);
        return org.springframework.http.ResponseEntity.ok("Baja publicada correctamente");
    }

    @Autowired
    private OfertaKafkaProducerService ofertaKafkaProducerService;

    // Endpoint para crear una oferta de donaciones
    @PostMapping("/ofertas/crear")
    public org.springframework.http.ResponseEntity<String> crearOferta(@RequestBody OfertaDonacionDTO oferta) {
        if (oferta == null || oferta.getIdOferta() == null || oferta.getIdOrganizacionDonante() == null || oferta.getDonaciones() == null) {
            return org.springframework.http.ResponseEntity.badRequest().body("Datos de oferta incompletos");
        }
        ofertaKafkaProducerService.enviarOferta(oferta);
        return org.springframework.http.ResponseEntity.ok("Oferta publicada correctamente");
    }

    @Autowired
    private TransferenciaKafkaConsumerService transferenciaKafkaConsumerService;

    @Autowired
    private com.example.api.service.SolicitudKafkaService solicitudKafkaService;

    @Autowired
    private com.example.api.service.SolicitudKafkaProducerService solicitudKafkaProducerService;

    @Autowired
    private TransferenciaKafkaProducerService transferenciaKafkaProducerService;

    @GetMapping("/externas")
    public List<com.example.api.dto.SolicitudDTO> listarExternas() {
        return solicitudKafkaService.getSolicitudesExternas();
    }

    @PostMapping("/crear")
    public void crearSolicitud(@RequestBody com.example.api.dto.SolicitudDTO solicitud) {
        solicitudKafkaProducerService.enviarSolicitud(solicitud);
    }

    // Endpoint para transferir donaciones
    @PostMapping("/transferencias/realizar")
    public org.springframework.http.ResponseEntity<String> transferirDonaciones(@RequestBody TransferenciaDonacionDTO transferencia) {
        if (transferencia == null || transferencia.getIdSolicitud() == null || transferencia.getIdOrganizacionDonante() == null || transferencia.getDonaciones() == null) {
            return org.springframework.http.ResponseEntity.badRequest().body("Datos de transferencia incompletos");
        }
        // Publicar en el topic correspondiente
        transferenciaKafkaProducerService.enviarTransferencia(transferencia.getIdSolicitud(), transferencia);
        // Aquí se debería actualizar el inventario (descontar/sumar), lógica a implementar
        return org.springframework.http.ResponseEntity.ok("Transferencia publicada correctamente");
    }

    // Inyección de ObjectMapper
    @Autowired
    private ObjectMapper objectMapper;

    // Endpoint para ver transferencias recibidas (en memoria)
    @GetMapping("/transferencias/recibidas")
    public List<String> verTransferenciasRecibidas() {
        return transferenciaKafkaConsumerService.getTransferenciasRecibidas();
    }
}
