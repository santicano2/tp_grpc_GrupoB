package com.example.web_services.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_services.entity.FiltroGuardado;

@Repository
public interface FiltroGuardadoRepository extends JpaRepository<FiltroGuardado, Long> {

    //Busca todos los filtros que pertenecen a un usuario específico.

    List<FiltroGuardado> findByUsuarioId(Long usuarioId);

    //Elimina un filtro por su ID y el ID del usuario
    
    @Transactional // Requerido para operaciones de modificación (DELETE, UPDATE)
    long deleteByIdAndUsuarioId(Long id, Long usuarioId);


    //Verifica si un filtro existe con ese ID y pertenece a ese usuario.

    boolean existsByIdAndUsuarioId(Long id, Long usuarioId);

}