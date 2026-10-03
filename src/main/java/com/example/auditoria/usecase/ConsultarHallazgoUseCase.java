package com.example.auditoria.usecase;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.ConteoCategoria;
import com.example.auditoria.usecase.port.PromedioCategoria;

import java.util.List;

public interface ConsultarHallazgoUseCase {
    List<CambioEstadoView> obtenerHistorial(HallazgoId id);
    List<PromedioCategoria> obtenerPromedioDiasRemediacionPorCategoria();
    List<ConteoCategoria> obtenerReabiertosPorCategoria();
}