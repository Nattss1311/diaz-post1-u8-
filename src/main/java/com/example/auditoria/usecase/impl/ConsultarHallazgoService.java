package com.example.auditoria.usecase.impl;

import java.util.List;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.ConsultarHallazgoUseCase;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.ConteoCategoria;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.HistorialAuditoriaPort;
import com.example.auditoria.usecase.port.PromedioCategoria;


public class ConsultarHallazgoService implements ConsultarHallazgoUseCase {

    private final HallazgoRepositoryPort repo;
    private final HistorialAuditoriaPort historial;

    public ConsultarHallazgoService(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        this.repo = repo;
        this.historial = historial;
    }

    @Override
    public List<CambioEstadoView> obtenerHistorial(HallazgoId id) {
        return historial.listarPorHallazgo(id);
    }

    @Override
    public List<PromedioCategoria> obtenerPromedioDiasRemediacionPorCategoria() {
        return repo.obtenerPromedioDiasRemediacionPorCategoria();
    }

    @Override
    public List<ConteoCategoria> obtenerReabiertosPorCategoria() {
        return repo.obtenerReabiertosPorCategoria();
    }
}