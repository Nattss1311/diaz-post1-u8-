package com.example.auditoria.usecase.impl;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.usecase.HallazgoNotFoundException;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;


public class IniciarRemediacionService implements IniciarRemediacionUseCase {

    private final HallazgoRepositoryPort repo;
    private final HistorialAuditoriaPort historial;

    public IniciarRemediacionService(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        this.repo = repo;
        this.historial = historial;
    }

    @Override
    @Transactional
    public void ejecutar(HallazgoId id, String responsable, LocalDate fechaCompromiso, String acciones) {
        HallazgoAuditoria hallazgo = repo.buscarPorId(id)
            .orElseThrow(() -> new HallazgoNotFoundException(id));

        PlanRemediacion plan = new PlanRemediacion(responsable, fechaCompromiso, acciones);
        EstadoHallazgo anterior = hallazgo.iniciarRemediacion(plan);

        repo.guardar(hallazgo);
        historial.registrar(id, anterior, EstadoHallazgo.EN_REMEDIACION, "Inicio de remediacion");
    }
}