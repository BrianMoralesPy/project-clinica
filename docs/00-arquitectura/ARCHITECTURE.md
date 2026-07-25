# Arquitectura — Sistema de Gestión Clínica

## Stack Tecnológico

### Backend
- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA + Hibernate
- PostgreSQL (Supabase)
- Flyway
- OpenAPI / Swagger
- MapStruct
- Lombok
- JUnit 5 + Spring Boot Test

### Frontend
- Angular 20
- TypeScript
- Angular Material
- Angular Signals
- RxJS
- Reactive Forms

### Base de Datos
- PostgreSQL alojado en Supabase
- Flyway para migraciones
- Supabase Storage para archivos

---

## Arquitectura General

El proyecto está dividido en dos aplicaciones independientes que se comunican mediante una API REST.

```
frontend/    → Angular 20 (SPA)
backend/     → Spring Boot 3 (API REST)
```

Toda la lógica de negocio pertenece al backend.
El frontend nunca accede directamente a la base de datos.

---

## Estructura Backend

```
backend/src/main/java/com/clinica/
├── ClinicaApplication.java
├── config/
│   ├── SecurityConfiguration.java
│   ├── CorsConfiguration.java
│   └── OpenApiConfiguration.java
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── UserDetailsServiceImpl.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BadRequestException.java
├── shared/
│   ├── BaseEntity.java
│   └── PaginatedResponse.java
└── module/
    ├── auth/
    │   ├── controller/
    │   ├── service/
    │   ├── dto/
    │   └── mapper/
    ├── usuario/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── entity/
    │   ├── dto/
    │   └── mapper/
    ├── paciente/
    ├── medico/
    ├── especialidad/
    ├── turno/
    └── archivo/
```

### Convenciones Backend
- Controllers reciben requests y devuelven DTOs
- Services contienen lógica de negocio
- Repositories acceden a la base de datos
- Nunca acceder al Repository desde un Controller
- Nunca exponer Entities en responses
- Usar Constructor Injection
- Separar request DTOs y response DTOs
- Centralizar errores con GlobalExceptionHandler

---

## Estructura Frontend

```
frontend/src/app/
├── core/
│   ├── auth/
│   │   ├── auth.service.ts
│   │   ├── auth.guard.ts
│   │   ├── auth.interceptor.ts
│   │   └── auth.model.ts
│   └── services/
├── shared/
│   ├── components/
│   ├── models/
│   └── pipes/
├── features/
│   ├── auth/
│   ├── dashboard/
│   ├── usuarios/
│   ├── pacientes/
│   ├── medicos/
│   ├── especialidades/
│   ├── turnos/
│   └── archivos/
├── layout/
│   ├── components/
│   │   ├── sidebar/
│   │   └── header/
│   └── layout.component.ts
└── app.routes.ts
```

### Convenciones Frontend
- Standalone Components (default Angular 20)
- Signals para estado local
- RxJS para operaciones asínceras
- Reactive Forms para formularios
- Lazy loading por feature
- OnPush change detection
- No lógica de negocio en componentes

---

## Seguridad

- Autenticación con JWT
- Spring Security para autorización
- Contraseñas cifradas con BCrypt
- Sesiones stateless
- Roles via JWT (evitar consultas a DB por request)

### Roles Iniciales
- ADMIN
- MEDICO
- RECEPCION

---

## Persistencia

- PostgreSQL en Supabase
- Flyway para migraciones
- Migraciones versionadas y trazables
- No editar esquema fuera de Flyway
- Nombres consistentes y explícitos

---

## Alcance Funcional

### MVP
- Autenticación y autorización
- Gestión de usuarios
- Gestión de roles
- Gestión de pacientes
- Gestión de médicos
- Gestión de especialidades
- Gestión de turnos
- Gestión de archivos (fotos, estudios, recetas)
- Dashboard inicial

### Diferido
- Historias clínicas complejas
- Permisos granulares
- Reportería avanzada
- Auditoría detallada
- Integraciones externas
- Notificaciones automáticas
- Multi-tenant
