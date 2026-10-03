package com.example.auditoria.config;

import org.springframework.transaction.annotation.Transactional;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;

public class ReabrirHallazgoTransaccional implements ReabrirHallazgoUseCase {

    private final ReabrirHallazgoUseCase delegado;

    public ReabrirHallazgoTransaccional(ReabrirHallazgoUseCase delegado) {
        this.delegado = delegado;
    }

    @Override
    @Transactional
    public void ejecutar(HallazgoId id, String motivo) {
        delegado.ejecutar(id, motivo);
    }
}