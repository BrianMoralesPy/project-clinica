# Fase 4 — Núcleo Funcional

## Objetivo
Implementar CRUD completo para todos los módulos del dominio: Pacientes, Médicos, Especialidades, Turnos, Archivos, Roles y Dashboard.

## Estado: COMPLETADA

## Backend — Módulos implementados

### Especialidad
- `Especialidad.java` — Entity
- `EspecialidadDtos.java` — DTOs (Crear, Actualizar, Response)
- `EspecialidadMapper.java` — Mapper manual
- `EspecialidadRepository.java` — Repository con existsByNombre
- `EspecialidadService.java` — CRUD completo
- `EspecialidadController.java` — API REST

### Paciente
- `Paciente.java` — Entity con enums (Sexo, GrupoSanguineo, EstadoPaciente)
- `PacienteDtos.java` — DTOs
- `PacienteMapper.java` — Mapper manual
- `PacienteRepository.java` — Repository con búsqueda paginada
- `PacienteService.java` — CRUD + cambio de estado
- `PacienteController.java` — API REST con roles ADMIN/RECEPCION

### Medico
- `Medico.java` — Entity con FK a Especialidad
- `MedicoDtos.java` — DTOs con EspecialidadResumen
- `MedicoMapper.java` — Mapper manual
- `MedicoRepository.java` — Repository con filtros
- `MedicoService.java` — CRUD + cambio de estado
- `MedicoController.java` — API REST con roles ADMIN/RECEPCION

### Turno
- `Turno.java` — Entity con FK a Paciente y Medico
- `TurnoDtos.java` — DTOs con ResumenPaciente/ResumenMedico
- `TurnoMapper.java` — Mapper manual
- `TurnoRepository.java` — Repository con filtros complejos
- `TurnoService.java` — CRUD + cancelar + completar
- `TurnoController.java` — API REST con roles según operación

### ArchivoAdjunto
- `ArchivoAdjunto.java` — Entity polimórfica
- `ArchivoAdjuntoResponse.java` — DTO
- `ArchivoMapper.java` — Mapper
- `ArchivoAdjuntoRepository.java` — Repository
- `ArchivoService.java` — Upload/download/delete con Supabase
- `SupabaseStorageService.java` — Cliente HTTP para Supabase Storage
- `ArchivoController.java` — API REST multipart

### Roles
- `RolDtos.java` — DTOs (Crear, Actualizar, Response)
- `RolMapper.java` — Mapper
- `RolService.java` — CRUD
- `RolController.java` — API REST

### Dashboard
- `DashboardResponse.java` — DTO
- `DashboardService.java` — Métricas agregadas
- `DashboardController.java` — API REST

## Frontend — Módulos implementados

### Shared
- `api.models.ts` — Interfaces TypeScript para todos los DTOs
- `api.service.ts` — Servicio HTTP genérico
- `domain.service.ts` — Servicios de dominio (PacienteService, MedicoService, etc.)

### Pacientes
- `paciente-list.component.ts` — Tabla paginada con filtros
- `paciente-form.component.ts` — Formulario reactive con validación

### Médicos
- `medico-list.component.ts` — Tabla paginada con filtro por especialidad
- `medico-form.component.ts` — Formulario reactive

### Especialidades
- `especialidad-list.component.ts` — Tabla con diálogo de edición
- `especialidad-form-dialog.component.ts` — Diálogo Material

### Turnos
- `turno-list.component.ts` — Tabla paginada con filtros
- `turno-form.component.ts` — Formulario con selección de paciente/médico

### Usuarios
- `usuario-list.component.ts` — Tabla paginada con búsqueda

### Dashboard
- `dashboard.component.ts` — Cards con métricas del sistema

## Endpoints implementados

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| GET | /api/pacientes | Listar (paginado) | ADMIN, RECEPCION |
| GET | /api/pacientes/{id} | Obtener | ADMIN, RECEPCION, MEDICO |
| POST | /api/pacientes | Crear | ADMIN, RECEPCION |
| PUT | /api/pacientes/{id} | Actualizar | ADMIN, RECEPCION |
| DELETE | /api/pacientes/{id} | Eliminar | ADMIN |
| PATCH | /api/pacientes/{id}/estado | Cambiar estado | ADMIN, RECEPCION |
| GET | /api/medicos | Listar (paginado) | ADMIN, RECEPCION |
| GET | /api/medicos/{id} | Obtener | ADMIN, RECEPCION |
| POST | /api/medicos | Crear | ADMIN |
| PUT | /api/medicos/{id} | Actualizar | ADMIN |
| DELETE | /api/medicos/{id} | Eliminar | ADMIN |
| GET | /api/especialidades | Listar | Todos autenticados |
| POST | /api/especialidades | Crear | ADMIN |
| PUT | /api/especialidades/{id} | Actualizar | ADMIN |
| DELETE | /api/especialidades/{id} | Eliminar | ADMIN |
| GET | /api/turnos | Listar (paginado) | ADMIN, RECEPCION, MEDICO |
| GET | /api/turnos/{id} | Obtener | ADMIN, RECEPCION, MEDICO |
| POST | /api/turnos | Crear | ADMIN, RECEPCION |
| PUT | /api/turnos/{id} | Reprogramar | ADMIN, RECEPCION |
| PATCH | /api/turnos/{id}/cancelar | Cancelar | ADMIN, RECEPCION |
| PATCH | /api/turnos/{id}/completar | Completar | MEDICO |
| GET | /api/roles | Listar | ADMIN |
| POST | /api/roles | Crear | ADMIN |
| PUT | /api/roles/{id} | Actualizar | ADMIN |
| POST | /api/archivos | Subir | ADMIN, RECEPCION, MEDICO |
| GET | /api/archivos | Listar por entidad | Autenticado |
| GET | /api/archivos/{id}/url | Obtener URL | Autenticado |
| DELETE | /api/archivos/{id} | Eliminar | ADMIN |
| GET | /api/dashboard | Métricas | ADMIN, RECEPCION, MEDICO |

## Próximos pasos
- Fase 5: Tests unitarios y de integración
- Fase 6: Historias clínicas, reportería, auditoría
