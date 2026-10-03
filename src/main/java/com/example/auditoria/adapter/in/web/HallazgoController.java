package com.example.auditoria.adapter.in.web;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.auditoria.domain.valueobject.Severidad;
import com.example.auditoria.domain.valueobject.HallazgoId;
import com.example.auditoria.usecase.CerrarHallazgoUseCase;
import com.example.auditoria.usecase.ConsultarHallazgoUseCase;
import com.example.auditoria.usecase.IniciarRemediacionUseCase;
import com.example.auditoria.usecase.ObtenerDashboardAuditoriaUseCase;
import com.example.auditoria.usecase.ReabrirHallazgoUseCase;
import com.example.auditoria.usecase.RegistrarHallazgoUseCase;
import com.example.auditoria.usecase.port.CambioEstadoView;
import com.example.auditoria.usecase.port.DashboardAuditoriaView;
import com.example.auditoria.usecase.port.ConteoCategoria;
import com.example.auditoria.usecase.port.PromedioCategoria;

@RestController
@RequestMapping("/api/hallazgos")
public class HallazgoController {

    private final RegistrarHallazgoUseCase registrarUseCase;
    private final ConsultarHallazgoUseCase consultarUseCase;
    private final ObtenerDashboardAuditoriaUseCase dashboardUseCase;
    private final IniciarRemediacionUseCase iniciarRemediacionUseCase;
    private final CerrarHallazgoUseCase cerrarHallazgoUseCase;
    private final ReabrirHallazgoUseCase reabrirHallazgoUseCase;

    public HallazgoController(
            RegistrarHallazgoUseCase registrarUseCase,
            ConsultarHallazgoUseCase consultarUseCase,
            ObtenerDashboardAuditoriaUseCase dashboardUseCase,
            IniciarRemediacionUseCase iniciarRemediacionUseCase,
            CerrarHallazgoUseCase cerrarHallazgoUseCase,
            ReabrirHallazgoUseCase reabrirHallazgoUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.consultarUseCase = consultarUseCase;
        this.dashboardUseCase = dashboardUseCase;
        this.iniciarRemediacionUseCase = iniciarRemediacionUseCase;
        this.cerrarHallazgoUseCase = cerrarHallazgoUseCase;
        this.reabrirHallazgoUseCase = reabrirHallazgoUseCase;
    }

    // DTO para recibir la creación del hallazgo
    // DTO para recibir la creación del hallazgo
    public record RegistrarHallazgoRequest(
            String titulo,
            String descripcion,
            String areaResponsable,
            String severidad
    ) {}

    // Endpoint para CREAR un nuevo Hallazgo
   // Endpoint para CREAR un nuevo Hallazgo
    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody RegistrarHallazgoRequest req) {
        Severidad severidadEnum = Severidad.valueOf(req.severidad().toUpperCase());

        HallazgoId id = registrarUseCase.ejecutar(
                req.titulo(),
                req.descripcion(),
                req.areaResponsable(),
                severidadEnum,
                LocalDate.now()
        );

return ResponseEntity.ok(id.toString());}
    // Checkpoint 1: GET /api/hallazgos/dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardAuditoriaView> obtenerDashboard() {
        DashboardAuditoriaView dashboard = dashboardUseCase.ejecutar();
        return ResponseEntity.ok(dashboard);
    }

    // Checkpoint 2: GET /api/hallazgos/{id}/historial
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<CambioEstadoView>> obtenerHistorial(@PathVariable String id) {
        HallazgoId hallazgoId = new HallazgoId(UUID.fromString(id));
        List<CambioEstadoView> historial = consultarUseCase.obtenerHistorial(hallazgoId);
        return ResponseEntity.ok(historial);
    }

    // DTOs auxiliares para transiciones
    public record IniciarRemediacionRequest(String responsable, LocalDate fechaLimite, String notas) {}
    public record ReabrirRequest(String motivo) {}

    // Transiciones de estado
    @PostMapping("/{id}/iniciar-remediacion")
    public ResponseEntity<Void> iniciarRemediacion(
            @PathVariable String id, 
            @RequestBody(required = false) IniciarRemediacionRequest req) {
        HallazgoId hallazgoId = new HallazgoId(UUID.fromString(id));
        String responsable = (req != null && req.responsable() != null) ? req.responsable() : "Sin asignar";
        LocalDate fechaLimite = (req != null && req.fechaLimite() != null) ? req.fechaLimite() : LocalDate.now().plusDays(15);
        String notas = (req != null && req.notas() != null) ? req.notas() : "Plan de remediación iniciado";
        
        iniciarRemediacionUseCase.ejecutar(hallazgoId, responsable, fechaLimite, notas);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<Void> cerrar(@PathVariable String id) {
        HallazgoId hallazgoId = new HallazgoId(UUID.fromString(id));
        cerrarHallazgoUseCase.ejecutar(hallazgoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrir(
            @PathVariable String id, 
            @RequestBody(required = false) ReabrirRequest req) {
        HallazgoId hallazgoId = new HallazgoId(UUID.fromString(id));
        String motivo = (req != null && req.motivo() != null) ? req.motivo() : "Reabierto por auditoría";
        
        reabrirHallazgoUseCase.ejecutar(hallazgoId, motivo);
        return ResponseEntity.ok().build();
    }

    // Métricas
    @GetMapping("/metricas/promedio-remediacion")
    public ResponseEntity<List<PromedioCategoria>> obtenerPromedioRemediacion() {
        List<PromedioCategoria> resultado = consultarUseCase.obtenerPromedioDiasRemediacionPorCategoria();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/metricas/reabiertos")
    public ResponseEntity<List<ConteoCategoria>> obtenerReabiertos() {
        List<ConteoCategoria> resultado = consultarUseCase.obtenerReabiertosPorCategoria();
        return ResponseEntity.ok(resultado);
    }
}