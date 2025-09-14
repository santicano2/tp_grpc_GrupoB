package com.example.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.EventDTO;
import com.example.api.service.EventoClientService;

import ong.events.Events;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    @PostMapping("/asignar")
    public EventDTO asignarMiembro(@RequestParam int eventoId,
                                @RequestParam String actor,
                                @RequestParam String username,
                                @RequestParam boolean add) {
        Events.MemberChangeRequest req = Events.MemberChangeRequest.newBuilder()
                .setActorUsername(actor)
                .setId(eventoId)
                .setUsername(username)
                .setAdd(add)
                .build();
        Events.Event event = service.getStub().assignOrRemoveMember(req);
        return mapToDTO(event);
    }

    private final EventoClientService service;

    public EventoController(EventoClientService service) {
        this.service = service;
    }

    @PostMapping("/crear")
    public EventDTO crear(@RequestParam String actor,
                        @RequestParam String nombre,
                        @RequestParam String descripcion,
                        @RequestParam String whenIso) {
                                    // Validar que la fecha sea futura
        LocalDateTime fechaEvento = LocalDateTime.parse(whenIso, DateTimeFormatter.ISO_DATE_TIME);
        Events.Event event = service.crearEvento(actor, nombre, descripcion, whenIso);
        return mapToDTO(event);
    }

    @GetMapping("/listar")
    public List<EventDTO> listar() {
        Events.EventList eventList = service.listarEventos();
        List<EventDTO> dtos = new ArrayList<>();
        for (Events.Event event : eventList.getEventsList()) {
            dtos.add(mapToDTO(event));
        }
        return dtos;
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<EventDTO> modificar(@PathVariable int id,  @RequestParam String actor, @RequestBody EventDTO eventDto) {
        try {
            eventDto.setId(id);
            // Validar que si se cambia la fecha, sea futura para eventos futuros
            LocalDateTime fechaEvento = LocalDateTime.parse(eventDto.getWhenIso(), DateTimeFormatter.ISO_DATE_TIME);
            if (fechaEvento.isBefore(LocalDateTime.now())) {
                // Caso de eventos pasados → registrar distribuciones
                // Por ahora devolvemos explícitamente 501 Not Implemented
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
            }
            Events.Event updatedEvent = service.modificarEvento(eventDto, actor);
            return new ResponseEntity<>(mapToDTO(updatedEvent), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/baja/{id}")
    public ResponseEntity<Void> bajaFisica(@PathVariable int id,  @RequestParam String actor) {
        try {
            service.bajaEvento(id, actor);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private EventDTO mapToDTO(Events.Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setWhenIso(event.getWhenIso());
        dto.setMembers(event.getMembersList()); // lista de usernames
        dto.setCreatedBy(event.getCreatedBy());
        dto.setModifiedBy(event.getModifiedBy());
        dto.setModificationDate(event.getModificationDate());
        return dto;
    }


}

