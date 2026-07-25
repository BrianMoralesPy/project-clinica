# Fase 2 — Base Técnica

## Objetivo
Configurar la base técnica del proyecto: Spring Boot backend, Angular frontend, base de datos y herramientas de desarrollo.

## Estado: COMPLETADA

## Archivos creados

### Backend
- `backend/pom.xml` — Configuración Maven con todas las dependencias
- `backend/src/main/java/com/clinica/ClinicaApplication.java` — Clase principal
- `backend/src/main/resources/application.yml` — Configuración principal
- `backend/src/main/resources/application-dev.yml` — Configuración de desarrollo
- `backend/src/main/resources/application-test.yml` — Configuración de tests
- `backend/src/main/resources/db/migration/V1-V11` — Migraciones Flyway

### Frontend
- `frontend/` — Proyecto Angular 21 creado con ng new
- `frontend/proxy.conf.json` — Proxy para conectar con Spring Boot
- `frontend/src/styles.scss` — Estilos globales con tema Angular Material

## Dependencias Backend
- Spring Boot 3.5.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT (jjwt 0.12.6)
- OpenAPI/Swagger (springdoc 2.6.0)
- MapStruct 1.6.3
- Lombok
- JUnit 5 + Spring Boot Test

## Dependencias Frontend
- Angular 21
- Angular Material 21
- Angular Signals
- RxJS

## Migraciones Flyway
| Migración | Contenido |
|-----------|-----------|
| V1 | Tabla usuario |
| V2 | Tablas rol y usuario_rol |
| V3 | Tablas permiso y rol_permiso |
| V4 | Tabla especialidad |
| V5 | Tabla paciente |
| V6 | Tabla medico |
| V7 | Tabla turno |
| V8 | Roles iniciales |
| V9 | Especialidades iniciales |
| V10 | Tabla archivo_adjunto |
| V11 | Buckets de Storage |

## Próximos pasos
- Fase 3: Seguridad (JWT + Spring Security + Auth endpoints)
