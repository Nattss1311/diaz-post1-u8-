package com.example.auditoria.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialCambioEstadoJpaRepository extends JpaRepository<HistorialCambioEstadoJpaEntity, Long> {
    List<HistorialCambioEstadoJpaEntity> findByHallazgoIdOrderByFechaAsc(String hallazgoId);
}