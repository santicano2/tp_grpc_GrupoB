package com.example.api.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.users.UsuariosServiceGrpc;
import ong.users.Users;
import org.springframework.stereotype.Service;

@Service
public class UsuarioClientService {

    private final UsuariosServiceGrpc.UsuariosServiceBlockingStub stub;

    public UsuarioClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.stub = UsuariosServiceGrpc.newBlockingStub(channel);
    }

    public Users.LoginResponse login(String login, String password) {
        Users.LoginRequest req = Users.LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();
        return stub.login(req);
    }

    public Users.CreateUserResponse crearUsuario(String actor, String username, String nombre,
                                                 String apellido, String email, Users.Role rol) {
        Users.CreateUserRequest req = Users.CreateUserRequest.newBuilder()
                .setActorUsername(actor)
                .setUsername(username)
                .setName(nombre)
                .setLastname(apellido)
                .setEmail(email)
                .setRole(rol)
                .build();
        return stub.createUser(req);
    }
}
