# AGENTS.md

# Sistema de Gestión para Clínica Odontológica

## Objetivo

Este proyecto desarrolla un sistema web para administrar una clínica odontológica.

El objetivo es construir un software mantenible, escalable y fácil de entender.

Antes de implementar una solución, prioriza la simplicidad y la claridad del código.

---

# Stack Tecnológico

## Frontend

- Angular 20
- TypeScript
- Angular Material
- Signals
- RxJS

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

## Base de Datos

- PostgreSQL (Supabase)

---

# Arquitectura

El proyecto está dividido en dos aplicaciones independientes.

```
frontend/
backend/
```

El frontend consume una API REST.

Toda la lógica de negocio pertenece al backend.

El frontend nunca accede directamente a la base de datos.

---

# Frontend (Angular)

## Organización

```
src/

core/
shared/
features/
layout/
assets/
environments/
```

Cada feature contiene:

```
feature/

components/
pages/
services/
models/
interfaces/
```

---

## Reglas Angular

Usar únicamente Standalone Components.

Preferir Signals para estado local.

Usar RxJS para operaciones asíncronas.

Toda llamada HTTP pertenece a un Service.

Los componentes contienen únicamente lógica de presentación.

Evitar componentes demasiado grandes.

Extraer lógica reutilizable a servicios.

Utilizar Angular Material.

No utilizar librerías UI adicionales sin autorización.

---

# Backend (Spring Boot)

Arquitectura:

```
controller/
service/
repository/
entity/
dto/
mapper/
config/
security/
exception/
```

---

## Reglas Java

Usar arquitectura por capas.

Controller

- recibe requests
- devuelve responses

Service

- contiene toda la lógica

Repository

- acceso a datos

Nunca acceder al Repository desde un Controller.

Nunca devolver Entities.

Siempre utilizar DTOs.

Preferir constructor injection.

Seguir principios SOLID.

---

# Base de Datos

La base de datos es PostgreSQL alojada en Supabase.

Acceso mediante Spring Data JPA.

Evitar SQL nativo salvo necesidad.

Gestionar migraciones mediante Flyway.

No modificar datos manualmente.

---

# API REST

La comunicación entre Angular y Spring Boot utiliza JSON.

Utilizar:

GET

POST

PUT

PATCH

DELETE

Respetar códigos HTTP.

Documentar endpoints mediante Swagger.

---

# Seguridad

JWT.

Spring Security.

BCrypt.

Nunca almacenar contraseñas en texto plano.

Implementar autorización por roles.

---

# Convenciones

## Angular

Componentes:

PacienteListComponent

Servicios:

PacienteService

Interfaces:

Paciente

Variables:

camelCase

Constantes:

UPPER_SNAKE_CASE

---

# Calidad

Siempre priorizar:

1. Correctitud
2. Simplicidad
3. Legibilidad
4. Reutilización
5. Rendimiento

Si existen dos soluciones válidas, elegir la más simple.

---

# Antes de escribir código

Analizar primero el código existente.

No reemplazar patrones ya utilizados.

Mantener consistencia con el resto del proyecto.

Reutilizar componentes y servicios existentes.

---

# No hacer

No modificar la arquitectura.

No crear archivos innecesarios.

No duplicar código.

No agregar dependencias sin motivo.

No introducir lógica de negocio en Angular.

No acceder a la base de datos desde el frontend.

No devolver Entities.

No utilizar any en TypeScript.

No ignorar errores.

No eliminar código sin explicación.

---

# Flujo de trabajo

Para tareas grandes:

1. Analizar.
2. Explicar el plan.
3. Implementar.
4. Verificar.
5. Resumir.

Para tareas pequeñas:

Implementar directamente.

Explicar únicamente las decisiones importantes.

---

# Respuestas

Cuando una decisión tenga varias alternativas:

- explicar ventajas
- explicar desventajas
- recomendar una

No asumir requisitos inexistentes.

Preguntar cuando exista ambigüedad.

---

# Prioridad de documentación

Consultar siempre en este orden:

1. AGENTS.md
2. PROJECT.md
3. ARCHITECTURE.md
4. README.md
5. Documentación oficial

Nunca inventar APIs.

Nunca inventar configuraciones.

Nunca asumir comportamiento no documentado.