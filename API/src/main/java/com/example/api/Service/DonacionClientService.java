package com.example.api.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.inventory.DonacionesServiceGrpc;
import ong.inventory.Inventory;
import com.google.protobuf.Empty;
import org.springframework.stereotype.Service;

@Service
public class DonacionClientService {

    private final DonacionesServiceGrpc.DonacionesServiceBlockingStub stub;

    public DonacionClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.stub = DonacionesServiceGrpc.newBlockingStub(channel);
    }

    public Inventory.DonationItem crearDonacion(String actor, Inventory.Category cat, String desc, int cantidad) {
        Inventory.CreateDonationRequest req = Inventory.CreateDonationRequest.newBuilder()
                .setActorUsername(actor)
                .setCategory(cat)
                .setDescription(desc)
                .setQuantity(cantidad)
                .build();
        return stub.createDonation(req);
    }

    public Inventory.DonationList listarDonaciones() {
        return stub.listDonations(Empty.getDefaultInstance());
    }
}
