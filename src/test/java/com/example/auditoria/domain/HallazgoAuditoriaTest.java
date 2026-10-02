package com.example.auditoria.domain;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.EstadoHallazgo;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.domain.valueobject.Severidad;

class HallazgoAuditoriaTest {

    @Test
    void debeTransicionarDeAbiertoAEnRemediacionYCerradoCorrectamente() {
        // Arrange: Crear entidad de dominio pura
        HallazgoAuditoria hallazgo = new HallazgoAuditoria(
            HallazgoId.nuevo(),
            "Servidor desactualizado",
            "Parches pendientes",
            "Sistemas",
            Severidad.ALTA,
            LocalDate.now()
        );

        assertEquals(EstadoHallazgo.ABIERTO, hallazgo.getEstado());

        // Act & Assert 1: Iniciar remediación
        PlanRemediacion plan = new PlanRemediacion("Carlos", LocalDate.now().plusDays(10), "Aplicar parches");
        hallazgo.iniciarRemediacion(plan);
        assertEquals(EstadoHallazgo.EN_REMEDIACION, hallazgo.getEstado());

        // Act & Assert 2: Cerrar hallazgo
        hallazgo.cerrar();
        assertEquals(EstadoHallazgo.CERRADO, hallazgo.getEstado());
        assertNotNull(hallazgo.getFechaCierre());
    }

    @Test
    void debeLanzarExcepcionAlCerrarSinPlanDeRemediacion() {
        HallazgoAuditoria hallazgo = new HallazgoAuditoria(
            HallazgoId.nuevo(),
            "Servidor desactualizado",
            "Parches pendientes",
            "Sistemas",
            Severidad.ALTA,
            LocalDate.now()
        );

        assertThrows(IllegalStateException.class, hallazgo::cerrar);
    }
}