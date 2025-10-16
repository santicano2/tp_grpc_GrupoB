package com.example.api.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.rest.dto.FiltroDTO;
import com.example.api.rest.service.FiltroService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/filtros")
public class FiltroController {

    private final FiltroService service;

    public FiltroController(FiltroService service) {
        this.service = service;
    }

    @GetMapping

    @Operation(summary = "Lista todos los filtros guardados")
    public List<FiltroDTO> listarFiltros() {
        return service.listarFiltros();
    }

    @PostMapping
    
    @Operation(summary = "Guarda un filtro nuevo")
    public FiltroDTO guardarFiltro(@RequestBody FiltroDTO filtro) {
        return service.guardarFiltro(filtro);
    }

    @PutMapping
    @Operation(summary = "Actualiza un filtro existente")
    public FiltroDTO actualizarFiltro(@RequestBody FiltroDTO filtro) {
        return service.actualizarFiltro(filtro);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un filtro por id")
    public boolean eliminarFiltro(@PathVariable Long id) {
        return service.eliminarFiltro(id);
    }
}
