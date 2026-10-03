package com.example.auditoria.adapter.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HallazgoJpaRepository extends JpaRepository<HallazgoJpaEntity, String> {

    @Query("SELECT h.severidad AS categoria, COUNT(h) AS total FROM HallazgoJpaEntity h GROUP BY h.severidad")
    List<ConteoProjection> contarPorSeveridad();

    @Query("SELECT h.estado AS categoria, COUNT(h) AS total FROM HallazgoJpaEntity h GROUP BY h.estado")
    List<ConteoProjection> contarPorEstado();

    @Query("SELECT h.areaResponsable AS categoria, " +
           "AVG(DATEDIFF(DAY, h.fechaDeteccion, h.fechaCierre)) AS promedio " +
           "FROM HallazgoJpaEntity h WHERE h.estado = com.example.auditoria.domain.valueobject.EstadoHallazgo.CERRADO " +
           "GROUP BY h.areaResponsable")
    List<PromedioProjection> promedioDiasCierrePorArea();

    @Query("SELECT h.severidad AS categoria, " +
           "AVG(DATEDIFF(DAY, h.fechaDeteccion, h.fechaCierre)) AS promedio " +
           "FROM HallazgoJpaEntity h WHERE h.fechaCierre IS NOT NULL " +
           "GROUP BY h.severidad")
    List<PromedioProjection> obtenerPromedioDiasRemediacionPorCategoria();

    @Query("SELECT h.severidad AS categoria, COUNT(h) AS total " +
           "FROM HallazgoJpaEntity h WHERE h.reabierto = true " +
           "GROUP BY h.severidad")
    List<ConteoProjection> obtenerReabiertosPorCategoria();

    interface ConteoProjection {
        String getCategoria();
        Long getTotal();
    }

    interface PromedioProjection {
        String getCategoria();
        Double getPromedio();
    }
}