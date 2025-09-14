package com.example.api.controller;

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

import com.example.api.dto.DonationItemDTO;
import com.example.api.service.DonacionClientService;

import ong.inventory.Inventory;

@RestController
@RequestMapping("/donaciones")
public class DonacionController {

    private final DonacionClientService service;

    public DonacionController(DonacionClientService service) {
        this.service = service;
    }

    @PostMapping("/crear")
    public DonationItemDTO crear(@RequestParam String actor,
                                @RequestParam String categoria,
                                @RequestParam String descripcion,
                                @RequestParam int cantidad) {
        Inventory.Category catEnum;
        try {
            catEnum = Inventory.Category.valueOf(categoria.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser uno de: ROPA, ALIMENTOS, JUGUETES, UTILES_ESCOLARES");
        }
        Inventory.DonationItem item = service.crearDonacion(actor, catEnum, descripcion, cantidad);
        return mapToDTO(item);
    }

        @GetMapping("/listar")
        public List<DonationItemDTO> listar() {
            Inventory.DonationList list = service.listarDonaciones();
            List<DonationItemDTO> dtos = new ArrayList<>();
            for (Inventory.DonationItem item : list.getItemsList()) {
                dtos.add(mapToDTO(item));
            }
            return dtos;
    }

        @PutMapping("/modificar/{id}")
    public ResponseEntity<DonationItemDTO> modificar(@PathVariable int id,  @RequestParam String actor, @RequestBody DonationItemDTO donationDTO) {
        if (id != donationDTO.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Inventory.DonationItem modifiedItem = service.modificarDonacion(donationDTO, actor);
            return new ResponseEntity<>(mapToDTO(modifiedItem), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id,  @RequestParam String actor) {
        try {
            service.bajaDonacion(id, actor);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        private DonationItemDTO mapToDTO(Inventory.DonationItem item) {
            DonationItemDTO dto = new DonationItemDTO();
            dto.setId(item.getId());
            dto.setCategory(item.getCategory().name());
            dto.setDescription(item.getDescription());
            dto.setQuantity(item.getQuantity());
            dto.setDeleted(item.getDeleted());
            dto.setCreatedAt(item.getCreatedAt());
            dto.setCreatedBy(item.getCreatedBy());
            dto.setUpdatedAt(item.getUpdatedAt());
            dto.setUpdatedBy(item.getUpdatedBy());
            return dto;
        }


}
