package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.dto.AspiranteDTO;
import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import ong.aspirantes.Aspirantes;
import ong.aspirantes.AspirantesServiceGrpc;

@Service
public class AspiranteClientService {

    private final AspirantesServiceGrpc.AspirantesServiceBlockingStub stub;

    @Autowired
    public AspiranteClientService(ManagedChannel channel) {
        this.stub = AspirantesServiceGrpc.newBlockingStub(channel);
    }

    public Aspirantes.Aspirante crearAspirante(String nombre, String apellido, Integer edad, 
                                              String telefono, String email, String motivo) {
        Aspirantes.CreateAspiranteRequest.Builder requestBuilder = Aspirantes.CreateAspiranteRequest.newBuilder()
                .setNombre(nombre)
                .setApellido(apellido)
                .setEmail(email)
                .setMotivo(motivo);
        
        if (edad != null && edad > 0) {
            requestBuilder.setEdad(edad);
        }
        if (telefono != null && !telefono.isEmpty()) {
            requestBuilder.setTelefono(telefono);
        }
        
        return stub.createAspirante(requestBuilder.build());
    }

    public Aspirantes.Aspirante obtenerAspirante(int id, String actor) {
        Aspirantes.AspiranteIdRequest request = Aspirantes.AspiranteIdRequest.newBuilder()
                .setActorUsername(actor)
                .setId(id)
                .build();
        return stub.getAspirante(request);
    }

    public Aspirantes.AspiranteList listarAspirantesPendientes() {
        return stub.listAspirantesPendientes(Empty.getDefaultInstance());
    }

    public Aspirantes.Aspirante rechazarAspirante(int id, String motivoRechazo, String actor) {
        Aspirantes.RejectAspiranteRequest request = Aspirantes.RejectAspiranteRequest.newBuilder()
                .setActorUsername(actor)
                .setId(id)
                .setMotivoRechazo(motivoRechazo)
                .build();
        return stub.rejectAspirante(request);
    }

    public Aspirantes.Aspirante aceptarAspirante(int id, String actor) {
        Aspirantes.AcceptAspiranteRequest request = Aspirantes.AcceptAspiranteRequest.newBuilder()
                .setActorUsername(actor)
                .setId(id)
                .build();
        return stub.acceptAspirante(request);
    }
}
