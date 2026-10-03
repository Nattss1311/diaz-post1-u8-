package com.example.auditoria.usecase.impl;

import org.springframework.transaction.annotation.Transactional;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.HallazgoNotFoundException;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;


public class ReabrirHallazgoService implements ReabrirHallazgoUseCase {

    private final HallazgoRepositoryPort repo;
    private final HistorialAuditoriaPort historial;

    public ReabrirHallazgoService(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        this.repo = repo;
        this.historial = historial;
    }

    @Override
    @Transactional
    public void ejecutar(HallazgoId id, String motivo) {
        HallazgoAuditoria hallazgo = repo.buscarPorId(id)
            .orElseThrow(() -> new HallazgoNotFoundException(id));

        // 1. Se llama sin parametros segun la entidad del profesor
        EstadoHallazgo anterior = hallazgo.reabrir(); 
        repo.guardar(hallazgo);

        // 2. El motivo se guarda en el historial de auditoria
        historial.registrar(id, anterior, EstadoHallazgo.ABIERTO, motivo);
    }
}