package com.example.auditoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.auditoria.usecase.*;
import com.example.auditoria.usecase.impl.*;
import com.example.auditoria.usecase.port.*;

@Configuration
public class AuditoriaConfiguration {

    @Bean
    public RegistrarHallazgoUseCase registrarHallazgoUseCase(HallazgoRepositoryPort repo) {
        return new RegistrarHallazgoService(repo);
    }

    @Bean
    public ConsultarHallazgoUseCase consultarHallazgoUseCase(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        return new ConsultarHallazgoService(repo, historial);
    }

    @Bean
    public IniciarRemediacionUseCase iniciarRemediacionUseCase(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        return new IniciarRemediacionService(repo, historial);
    }

    @Bean
    public CerrarHallazgoUseCase cerrarHallazgoUseCase(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        return new CerrarHallazgoService(repo, historial);
    }

    @Bean
    public ReabrirHallazgoUseCase reabrirHallazgoUseCase(HallazgoRepositoryPort repo, HistorialAuditoriaPort historial) {
        return new ReabrirHallazgoService(repo, historial);
    }

    @Bean
    public ObtenerDashboardAuditoriaUseCase obtenerDashboardAuditoriaUseCase(HallazgoRepositoryPort repo) {
        return new ObtenerDashboardAuditoriaService(repo);
    }
}