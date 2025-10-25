package com.example.web_services.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.web_services.dto.FiltroDTO;
import com.example.web_services.entity.FiltroGuardado;
import com.example.web_services.entity.FiltroGuardado.TipoFiltro;
import com.example.web_services.repository.FiltroGuardadoRepository;

@Service
public class FiltroService {

    private final FiltroGuardadoRepository repository;

    public FiltroService(FiltroGuardadoRepository repository) {
        this.repository = repository;
    }

//      MAPEOS
    private FiltroDTO toDTO(FiltroGuardado entidad) {
        FiltroDTO dto = new FiltroDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setCriteriosJson(entidad.getFiltrosJson());
        return dto;
    }

    private FiltroGuardado toEntity(FiltroDTO dto) {
        FiltroGuardado entidad = new FiltroGuardado();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setFiltrosJson(dto.getCriteriosJson()); 
        entidad.setTipo(TipoFiltro.EVENTOS);
        return entidad;
    }

//      METODOS
    public List<FiltroDTO> listarFiltros(Long usuarioId) {
        List<FiltroGuardado> entidades = repository.findByUsuarioId(usuarioId);
        return entidades.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FiltroDTO guardarFiltro(FiltroDTO filtro, Long usuarioId) {
        FiltroGuardado entidad = toEntity(filtro);
        entidad.setUsuarioId(usuarioId);
        entidad.setId(null);
        FiltroGuardado entidadGuardada = repository.save(entidad);
        return toDTO(entidadGuardada);
    }

    public boolean eliminarFiltro(Long id, Long usuarioId) {
        if (repository.existsByIdAndUsuarioId(id, usuarioId)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public FiltroDTO actualizarFiltro(FiltroDTO filtro, Long usuarioId) {
        if (filtro.getId() == null || !repository.existsByIdAndUsuarioId(filtro.getId(), usuarioId)) {
            return null; 
        }
        FiltroGuardado entidad = toEntity(filtro);
        entidad.setUsuarioId(usuarioId);
        FiltroGuardado entidadActualizada = repository.save(entidad);
        return toDTO(entidadActualizada);
    }
}