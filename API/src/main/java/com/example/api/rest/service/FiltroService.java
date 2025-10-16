package com.example.api.rest.service;

import com.example.api.rest.dto.FiltroDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FiltroService {

    private final List<FiltroDTO> filtros = new ArrayList<>();

    public List<FiltroDTO> listarFiltros() {
        return filtros;
    }

    public FiltroDTO guardarFiltro(FiltroDTO filtro) {
        filtro.setId((long)(filtros.size()+1));
        filtros.add(filtro);
        return filtro;
    }

    public boolean eliminarFiltro(Long id) {
        return filtros.removeIf(f -> f.getId().equals(id));
    }

    public FiltroDTO actualizarFiltro(FiltroDTO filtro) {
        for(int i=0;i<filtros.size();i++){
            if(filtros.get(i).getId().equals(filtro.getId())){
                filtros.set(i, filtro);
                return filtro;
            }
        }
        return null;
    }
}
