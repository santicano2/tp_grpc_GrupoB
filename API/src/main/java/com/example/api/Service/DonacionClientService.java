package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.dto.DonationItemDTO;
import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ong.inventory.DonacionesServiceGrpc;
import ong.inventory.Inventory;

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

    public Inventory.DonationItem modificarDonacion(DonationItemDTO donationDto, String actor) {
        Inventory.UpdateDonationRequest request = Inventory.UpdateDonationRequest.newBuilder()
            .setId(donationDto.getId())
            .setDescription(donationDto.getDescription())
            .setQuantity(donationDto.getQuantity())
            .setActorUsername(actor)
            .build();
        return stub.updateDonation(request);
    }

    public void bajaDonacion(int id, String actor) {
        Inventory.DonationIdRequest request = Inventory.DonationIdRequest.newBuilder()
            .setId(id)
            .setActorUsername(actor)
            .build();
        stub.deleteDonation(request);
    }
}
