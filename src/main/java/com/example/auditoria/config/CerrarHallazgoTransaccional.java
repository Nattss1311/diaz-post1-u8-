package com.example.auditoria.config;

import org.springframework.transaction.annotation.Transactional;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.CerrarHallazgoUseCase;

public class CerrarHallazgoTransaccional implements CerrarHallazgoUseCase {

    private final CerrarHallazgoUseCase delegado;

    public CerrarHallazgoTransaccional(CerrarHallazgoUseCase delegado) {
        this.delegado = delegado;
    }

    @Override
    @Transactional
    public void ejecutar(HallazgoId id) {
        delegado.ejecutar(id);
    }
}