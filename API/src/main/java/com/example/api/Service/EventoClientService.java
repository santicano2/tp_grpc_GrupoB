package com.example.api.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.dto.EventDTO;
import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import ong.events.EventosServiceGrpc;
import ong.events.Events;

@Service
public class EventoClientService {
    public EventosServiceGrpc.EventosServiceBlockingStub getStub() {
        return stub;
    }

    private final EventosServiceGrpc.EventosServiceBlockingStub stub;

    @Autowired
    public EventoClientService(ManagedChannel channel) {
        this.stub = EventosServiceGrpc.newBlockingStub(channel);
    }

    public Events.Event crearEvento(String actor, String name, String desc, String whenIso) {
        Events.CreateEventRequest req = Events.CreateEventRequest.newBuilder()
                .setActorUsername(actor)
                .setName(name)
                .setDescription(desc)
                .setWhenIso(whenIso)
                .build();
        Events.Event event = stub.createEvent(req);

        // Si el backend no lo setea, aseguramos que created_by quede registrado
        return Events.Event.newBuilder(event)
                .setCreatedBy(actor)
                .build();
    }

    public Events.EventList listarEventos() {
        return stub.listEvents(Empty.getDefaultInstance());
    }

    public Events.Event modificarEvento(EventDTO eventDto, String actor) {
        Events.UpdateEventRequest request = Events.UpdateEventRequest.newBuilder()
            .setActorUsername(actor)
            .setId(eventDto.getId())
            .setName(eventDto.getName())
            .setDescription(eventDto.getDescription())
            .setWhenIso(eventDto.getWhenIso())
            .build();
        Events.Event event = stub.updateEvent(request);

        // Seteamos quien modificó y la fecha
        return Events.Event.newBuilder(event)
                .setModifiedBy(actor)
                .setModificationDate(Instant.now().toString())
                .build();
    }

    public void bajaEvento(int id, String actor) {
        Events.EventIdRequest request = Events.EventIdRequest.newBuilder()
            .setActorUsername(actor)
            .setId(id)
            .build();
        stub.deleteFutureEvent(request);
    }
}
