package com.example.auditoria.usecase;

import java.util.List;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;

public interface ConsultarHallazgoUseCase {
    HallazgoAuditoria obtenerPorId(HallazgoId id);
    List<HallazgoAuditoria> obtenerTodos();
}