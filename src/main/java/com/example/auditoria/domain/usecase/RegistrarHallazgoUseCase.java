package com.example.auditoria.usecase;

import java.time.LocalDate;

import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.Severidad;

public interface RegistrarHallazgoUseCase {
    HallazgoId ejecutar(String titulo, String descripcion, String areaResponsable,
                         Severidad severidad, LocalDate fechaDeteccion);
}