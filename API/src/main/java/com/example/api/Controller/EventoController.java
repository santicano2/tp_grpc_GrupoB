package com.example.api.controller;

import ong.events.Events;
import com.example.api.dto.EventDTO;
import java.util.List;
import java.util.ArrayList;
import com.example.api.service.EventoClientService;
import org.springframework.web.bind.annotation.*;

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

    private EventDTO mapToDTO(Events.Event event) {
    EventDTO dto = new EventDTO();
    dto.setId(event.getId());
    dto.setNombre(event.getName());
    dto.setDescripcion(event.getDescription());
    dto.setFechaEvento(event.getWhenIso());
    // Si tienes usuarioCreacion, modificadoPor, fechaModificacion en el Protobuf, agrégalos aquí
    return dto;
    }
}
