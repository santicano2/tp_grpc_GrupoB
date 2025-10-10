package com.example.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.AdhesionEventoDTO;
import com.example.api.dto.BajaEventoDTO;
import com.example.api.dto.EventDTO;
import com.example.api.dto.PublicarEventoDTO;
import com.example.api.service.EventoKafkaConsumerService;
import com.example.api.service.EventoKafkaProducerService;

@RestController
@RequestMapping("/api/eventos-kafka")
public class EventoControllerKafka {

    @Autowired
    private EventoKafkaProducerService eventoKafkaProducerService;

    @Autowired
    private EventoKafkaConsumerService eventoKafkaConsumerService;

    // Publicar evento solidario
    @PostMapping("/publicar")
    public ResponseEntity<String> publicarEvento(@RequestBody PublicarEventoDTO evento) {
        if (evento == null || evento.getIdOrganizacion() == null || evento.getIdEvento() == null
                || evento.getNombre() == null || evento.getDescripcion() == null || evento.getFechaHora() == null) {
            return ResponseEntity.badRequest().body("Datos de evento incompletos");
        }
        eventoKafkaProducerService.publicarEvento(evento);
        return ResponseEntity.ok("Evento publicado correctamente");
    }

    // Dar de baja un evento solidario
    @PostMapping("/baja")
    public ResponseEntity<String> bajaEvento(@RequestBody BajaEventoDTO baja) {
        if (baja == null || baja.getIdOrganizacion() == null || baja.getIdEvento() == null) {
            return ResponseEntity.badRequest().body("Datos de baja incompletos");
        }
        eventoKafkaProducerService.bajaEvento(baja);
        return ResponseEntity.ok("Baja de evento publicada correctamente");
    }

    // Adherirse a evento externo
    @PostMapping("/adherir-evento/{idOrganizador}")
    public ResponseEntity<String> adherirEvento(
            @PathVariable String idOrganizador,
            @RequestBody AdhesionEventoDTO adhesion) {
        if (adhesion == null || adhesion.getIdEvento() == null || adhesion.getIdOrganizacion() == null) {
            return ResponseEntity.badRequest().body("Datos de adhesión incompletos");
        }
        eventoKafkaProducerService.adherirEvento(idOrganizador, adhesion);
        return ResponseEntity.ok("Adhesión enviada correctamente al organizador " + idOrganizador);
    }

    // Listar eventos externos
    @GetMapping("/externos")
    public List<EventDTO> listarEventosExternos() {
        return eventoKafkaConsumerService.getEventosExternos();
    }

    // Listar adhesiones recibidas
    @GetMapping("/adhesiones")
    public List<AdhesionEventoDTO> listarAdhesiones() {
        return eventoKafkaConsumerService.getAdhesiones();
    }
}
