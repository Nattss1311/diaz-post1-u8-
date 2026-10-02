package com.example.auditoria.adapter.in.web.dto;

import java.time.LocalDate;

import com.example.auditoria.domain.valueobject.Severidad;

public record RegistrarHallazgoRequest(
    String titulo,
    String descripcion,
    String areaResponsable,
    Severidad severidad,
    LocalDate fechaDeteccion
) {}