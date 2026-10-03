package com.example.auditoria.config;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;

public class IniciarRemediacionTransaccional implements IniciarRemediacionUseCase {

    private final IniciarRemediacionUseCase delegado;

    public IniciarRemediacionTransaccional(IniciarRemediacionUseCase delegado) {
        this.delegado = delegado;
    }

    @Override
    @Transactional
    public void ejecutar(HallazgoId id, String responsable, LocalDate fechaLimite, String notas) {
        delegado.ejecutar(id, responsable, fechaLimite, notas);
    }
}