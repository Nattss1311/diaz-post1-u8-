package com.example.auditoria.usecase.port;

import java.util.List;
import java.util.Optional;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;

public interface HallazgoRepositoryPort {
    void guardar(HallazgoAuditoria hallazgo);
    Optional<HallazgoAuditoria> buscarPorId(HallazgoId id);
    List<HallazgoAuditoria> buscarTodos();
}