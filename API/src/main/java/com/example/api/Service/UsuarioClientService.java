package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.dto.UserDTO;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.users.Users;
import ong.users.UsuariosServiceGrpc;

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
                                                String apellido, String email, String phone, Users.Role rol) {
        if (phone == null) phone = "";
        Users.CreateUserRequest req = Users.CreateUserRequest.newBuilder()
                .setActorUsername(actor)
                .setUsername(username)
                .setName(nombre)
                .setLastname(apellido)
                .setEmail(email)
                .setPhone(phone)
                .setRole(rol)
                .build();
        return stub.createUser(req);
    }

    public Users.User modificarUsuario(UserDTO userDto, String actor) {
        Users.UpdateUserRequest request = Users.UpdateUserRequest.newBuilder()
            .setActorUsername(actor)
            .setId(userDto.getId()) 
            .setUsername(userDto.getUsername())
            .setName(userDto.getName())
            .setLastname(userDto.getLastname())
            .setEmail(userDto.getEmail())
            .setPhone(userDto.getPhone())
            .setRole(Users.Role.valueOf(userDto.getRole()))
            .setActive(userDto.isActive())
            .build();
        return stub.updateUser(request);
    }

    public Users.User bajaUsuario(int id, String actor) {
        Users.DeactivateUserRequest request = Users.DeactivateUserRequest.newBuilder()
            .setActorUsername(actor)
            .setId(id)
            .build();
        return stub.deactivateUser(request);
    }

    public Users.UserList listarUsuarios() {
        return stub.listUsers(com.google.protobuf.Empty.getDefaultInstance());
    }
}
