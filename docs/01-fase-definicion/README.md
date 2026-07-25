# Fase 1 — Definición

## Objetivo
Definir el alcance del MVP, el modelo de dominio, el esquema de base de datos y los contratos de API antes de escribir código.

## Estado: COMPLETADA

## Archivos creados
- `docs/00-arquitectura/ARCHITECTURE.md` — Decisiones técnicas, stack, convenciones
- `docs/00-arquitectura/DATABASE.md` — Esquema completo de BD con todas las tablas
- `docs/00-arquitectura/API-CONTRACTS.md` — Contratos de API para todos los módulos
- `docs/00-arquitectura/DOMAIN-MODEL.md` — Modelo de dominio, reglas de negocio, estados

## Decisiones tomadas

### Stack Tecnológico
- Backend: Java 21 + Spring Boot 3 + Spring Security + Spring Data JPA
- Frontend: Angular 20 + Angular Material + Signals + RxJS
- BD: PostgreSQL en Supabase + Flyway para migraciones
- Storage: Supabase Storage para archivos (fotos, estudios, recetas, documentos)

### Modelo de Dominio
- 8 entidades base: Usuario, Rol, Permiso, Paciente, Médico, Especialidad, Turno, Archivo Adjunto
- Estados explícitos en lugar de booleanos
- Relaciones normalizadas y consistentes
- Polymorphic association para archivos adjuntos

### Seguridad
- JWT con roles embebidos
- BCrypt para contraseñas
- Sesiones stateless
- Roles iniciales: ADMIN, MEDICO, RECEPCION

### Alcance MVP
- Autenticación y autorización
- Gestión de usuarios, roles, pacientes, médicos, especialidades, turnos
- Gestión de archivos (fotos, estudios, recetas, documentos)
- Dashboard inicial

### Funcionalidades diferidas
- Historias clínicas complejas
- Permisos granulares
- Reportería avanzada
- Auditoría detallada
- Integraciones externas
- Notificaciones automáticas
- Multi-tenant

## Pendientes para Fase 2
- Crear proyecto Spring Boot
- Crear proyecto Angular
- Configurar Flyway y crear migraciones
- Configurar conexión a Supabase
- Configurar OpenAPI/Swagger
