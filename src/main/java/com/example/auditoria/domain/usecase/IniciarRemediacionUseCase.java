package com.example.auditoria.usecase;

import java.time.LocalDate;

import com.example.auditoria.domain.valueobject.HallazgoId;

public interface IniciarRemediacionUseCase {
    void ejecutar(HallazgoId id, String responsable, LocalDate fechaLimite, String notas);
}