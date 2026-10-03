package com.example.auditoria.usecase.port;

import java.util.List;
import java.util.Optional;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;

public interface HallazgoRepositoryPort {
    void guardar(HallazgoAuditoria hallazgo);
    Optional<HallazgoAuditoria> buscarPorId(HallazgoId id);
    List<HallazgoAuditoria> buscarTodos(); // <- Método que faltaba por implementar

    // Métodos del Dashboard / Consultas
    List<ConteoCategoria> contarPorSeveridad();
    List<ConteoCategoria> contarPorEstado();
    List<PromedioCategoria> promedioDiasCierrePorArea();
    List<PromedioCategoria> obtenerPromedioDiasRemediacionPorCategoria(); // <- Requerido por ConsultarHallazgoService
    List<ConteoCategoria> obtenerReabiertosPorCategoria();               // <- Requerido por ConsultarHallazgoService
}