package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.users.Users;
import ong.users.UsuariosServiceGrpc;

public class Cliente {
    public static void main(String[] args) {
        // Crear canal hacia el server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Crear stub
        UsuariosServiceGrpc.UsuariosServiceBlockingStub stub = UsuariosServiceGrpc.newBlockingStub(channel);

        // Hacer llamado
        Users.LoginRequest request = Users.LoginRequest.newBuilder()
            .setLogin("presidente1")
            .setPassword("password123")
            .build();

        Users.LoginResponse response = stub.login(request);

        System.out.println("Respuesta del servidor: " + response);

        channel.shutdown();
    }
}
