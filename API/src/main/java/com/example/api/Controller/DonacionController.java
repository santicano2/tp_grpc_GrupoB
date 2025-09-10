package com.example.api.controller;

import ong.inventory.Inventory;
import com.example.api.service.DonacionClientService;
import com.example.api.dto.DonationItemDTO;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;

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
