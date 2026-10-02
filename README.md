# Post-contenido — Patrones Arquitectónicos II: Clean Architecture

Proyecto Spring Boot para la gestión e inspección de auditorías (`AuditoriaHallazgos`), estructurado bajo **Clean Architecture** (Arquitectura Limpia) con desacoplamiento total del dominio respecto a frameworks externos.

---

## 1. Estructura de Capas (Los 4 Círculos de Clean Architecture)

- **Domain (`domain/`):** Contiene el Aggregate Root `HallazgoAuditoria`, Enums (`EstadoHallazgo`, `Severidad`) y Value Objects (`HallazgoId`, `PlanRemediacion`). Sin ninguna dependencia externa ni framework.
- **Use Cases (`usecase/`):** Contiene los casos de uso (`Registrar`, `IniciarRemediacion`, `Cerrar`, `Reabrir`, `Consultar`), sus interfaces (`port/`) e implementaciones (`impl/`). Depende únicamente del Dominio.
- **Adapters (`adapter/`):** 
  - `in/web/`: Controlador REST (`HallazgoController`), DTOs y Mappers.
  - `out/persistence/`: Repositorio JPA, Entidades JPA (`HallazgoEntity`) y Adapter de persistencia.
- **Configuration (`config/`):** Inyección de dependencias y wiring de Beans en Spring Boot (`AuditoriaConfiguration`).

```text
src/main/java/com/example/auditoria/
├── domain/
│   ├── entity/
│   │   └── HallazgoAuditoria.java
│   └── valueobject/
│       ├── EstadoHallazgo.java
│       ├── HallazgoId.java
│       ├── PlanRemediacion.java
│       ├── Severidad.java
│       └── TransicionInvalidaException.java
├── usecase/
│   ├── port/
│   │   └── HallazgoRepositoryPort.java
│   ├── impl/
│   │   ├── CerrarHallazgoService.java
│   │   ├── ConsultarHallazgoService.java
│   │   ├── IniciarRemediacionService.java
│   │   ├── ReabrirHallazgoService.java
│   │   └── RegistrarHallazgoService.java
│   ├── CerrarHallazgoUseCase.java
│   ├── ConsultarHallazgoUseCase.java
│   ├── IniciarRemediacionUseCase.java
│   ├── ReabrirHallazgoUseCase.java
│   └── RegistrarHallazgoUseCase.java
├── adapter/
│   ├── in/web/
│   │   ├── dto/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── HallazgoController.java
│   │   └── HallazgoMapper.java
│   └── out/persistence/
│       ├── HallazgoEntity.java
│       ├── HallazgoJpaRepository.java
│       └── HallazgoRepositoryAdapter.java
└── config/
    └── AuditoriaConfiguration.java
```
## 2. Decisiones de Diseño de la Parte 1

**1. Modelado de `Severidad` vs. `EstadoHallazgo`**  
**`Severidad` (`BAJA`, `MEDIA`, `ALTA`, `CRITICA`):** Representa un atributo estático para la clasificación del nivel de riesgo del hallazgo.  
**`EstadoHallazgo` (`ABIERTO`, `EN_REMEDIACION`, `CERRADO`, `REABIERTO`):** Modela una máquina de estados explícita que controla y valida las transiciones de negocio permitidas dentro del Agregado.

**2. Límite del Agregado con `PlanRemediacion`**  
`PlanRemediacion` se diseñó como un **Value Object inmutable embebido** dentro de `HallazgoAuditoria` (en lugar de una Entidad separada). Esto garantiza la consistencia transaccional e invariantes de negocio: un hallazgo no puede pasar al estado `EN_REMEDIACION` o `CERRADO` sin un plan válido dentro del mismo límite de agregación.

---

## 3. Instrucciones de Ejecución

1. **Compilar el proyecto:**
   ```bash
   mvn clean compile
   ```
2. **Ejecutar pruebas unitarias de dominio (sin Spring):**

```Bash
mvn test
```
3. **Iniciar la aplicación:**

```Bash
mvn spring-boot:run
```

## 4. Evidencias de Pruebas (Checkpoints)

| Caso de Prueba / Endpoint | Método HTTP | Estado Esperado | Evidencia Visual |
| :--- | :---: | :---: | :---: |
| **Registrar Hallazgo** | `POST` | `201 Created` | <img src="./images/paso6-registrar-hallazgo.png" width="400" alt="Registrar Hallazgo"> |
| **Iniciar Remediación** | `PATCH` | `200 OK` | <img src="./images/paso6-iniciar-remediacion.png" width="400" alt="Iniciar Remediación"> |
| **Cerrar Hallazgo** | `PATCH` | `200 OK` | <img src="./images/paso6-cerrar-hallazgo.png" width="400" alt="Cerrar Hallazgo"> |
| **Reabrir Hallazgo** | `PATCH` | `200 OK` | <img src="./images/paso6-reabrir-hallazgo.png" width="400" alt="Reabrir Hallazgo"> |
| **Error al Cerrar Sin Plan** | `PATCH` | `400 Bad Request` | <img src="./images/paso6-error-cerrar-sin-plan.png" width="400" alt="Error Cierre Sin Plan"> |
| **Pruebas Unitarias JUnit** | `mvn test` | `BUILD SUCCESS` | <img src="./images/paso6-tests-junit.png" width="400" alt="Pruebas JUnit"> |