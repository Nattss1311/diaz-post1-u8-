package com.example.auditoria.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.auditoria.domain.entity.HallazgoAuditoria;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.domain.valueobject.PlanRemediacion;
import com.example.auditoria.usecase.port.ConteoCategoria;
import com.example.auditoria.usecase.port.HallazgoRepositoryPort;
import com.example.auditoria.usecase.port.PromedioCategoria;

@Component
public class HallazgoRepositoryAdapter implements HallazgoRepositoryPort {

    private final HallazgoJpaRepository jpa;

    public HallazgoRepositoryAdapter(HallazgoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(HallazgoAuditoria hallazgo) {
        jpa.save(toEntity(hallazgo));
    }

    @Override
    public Optional<HallazgoAuditoria> buscarPorId(HallazgoId id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public List<HallazgoAuditoria> buscarTodos() {
        return jpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    // --- Métodos de Consultas / Métricas ---

    @Override
    public List<ConteoCategoria> contarPorSeveridad() {
        return jpa.contarPorSeveridad().stream()
                .map(p -> new ConteoCategoria(
                        p.getCategoria() != null ? p.getCategoria() : "SIN_SEVERIDAD", 
                        p.getTotal()))
                .toList();
    }

    @Override
    public List<ConteoCategoria> contarPorEstado() {
        return jpa.contarPorEstado().stream()
                .map(p -> new ConteoCategoria(
                        p.getCategoria() != null ? p.getCategoria() : "SIN_ESTADO", 
                        p.getTotal()))
                .toList();
    }

    @Override
    public List<PromedioCategoria> promedioDiasCierrePorArea() {
        return jpa.promedioDiasCierrePorArea().stream()
                .map(p -> new PromedioCategoria(
                        p.getCategoria(), 
                        p.getPromedio() != null ? p.getPromedio() : 0.0))
                .toList();
    }

    @Override
    public List<PromedioCategoria> obtenerPromedioDiasRemediacionPorCategoria() {
        return jpa.obtenerPromedioDiasRemediacionPorCategoria().stream()
                .map(p -> new PromedioCategoria(
                        p.getCategoria() != null ? p.getCategoria() : "GENERAL", 
                        p.getPromedio() != null ? p.getPromedio() : 0.0))
                .toList();
    }

    @Override
    public List<ConteoCategoria> obtenerReabiertosPorCategoria() {
        return jpa.obtenerReabiertosPorCategoria().stream()
                .map(p -> new ConteoCategoria(
                        p.getCategoria() != null ? p.getCategoria() : "GENERAL", 
                        p.getTotal()))
                .toList();
    }

    // --- Métodos de mapeo Dominio <-> Entidad JPA ---

  private HallazgoAuditoria toDomain(HallazgoJpaEntity e) {
        PlanRemediacion plan = e.getPlanResponsable() != null
                ? new PlanRemediacion(e.getPlanResponsable(), e.getPlanFechaLimite(), e.getPlanNotas())
                : null;

        return new HallazgoAuditoria(
                new HallazgoId(UUID.fromString(e.getId())),
                e.getTitulo(),
                e.getDescripcion(),
                e.getAreaResponsable(),
                e.getSeveridad(),
                e.getFechaDeteccion(),
                e.getEstado(),
                plan,
                e.getFechaCierre()
        );
    }
    private HallazgoJpaEntity toEntity(HallazgoAuditoria h) {
        HallazgoJpaEntity e = new HallazgoJpaEntity();
        e.setId(h.getId() != null ? h.getId().toString() : null);
        e.setTitulo(h.getTitulo());
        e.setDescripcion(h.getDescripcion());
        e.setAreaResponsable(h.getAreaResponsable());
        e.setSeveridad(h.getSeveridad());
        e.setEstado(h.getEstado());
        e.setFechaDeteccion(h.getFechaDeteccion());
        e.setFechaCierre(h.getFechaCierre());

        if (h.getPlanRemediacion() != null) {
            e.setPlanResponsable(h.getPlanRemediacion().responsable());
            e.setPlanFechaLimite(h.getPlanRemediacion().fechaLimite());
            e.setPlanNotas(h.getPlanRemediacion().notas());
        }

        return e;
    }
}