package com.example.api.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.events.EventosServiceGrpc;
import ong.events.Events;
import com.google.protobuf.Empty;
import org.springframework.stereotype.Service;

@Service
public class EventoClientService {
    public EventosServiceGrpc.EventosServiceBlockingStub getStub() {
        return stub;
    }

    private final EventosServiceGrpc.EventosServiceBlockingStub stub;

    public EventoClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.stub = EventosServiceGrpc.newBlockingStub(channel);
    }

    public Events.Event crearEvento(String actor, String nombre, String desc, String whenIso) {
        Events.CreateEventRequest req = Events.CreateEventRequest.newBuilder()
                .setActorUsername(actor)
                .setName(nombre)
                .setDescription(desc)
                .setWhenIso(whenIso)
                .build();
        return stub.createEvent(req);
    }

    public Events.EventList listarEventos() {
        return stub.listEvents(Empty.getDefaultInstance());
    }
}
