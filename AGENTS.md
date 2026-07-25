# AGENTS.md — Sistema de Gestión para Clínica General

## Propósito

Este proyecto consiste en el desarrollo de un sistema web de gestión para una clínica general, orientado a la administración de usuarios, pacientes, médicos, turnos y procesos operativos básicos.

El objetivo principal es construir una base de software **escalable, mantenible y consistente**, con una arquitectura clara desde el inicio y preparada para crecer por módulos funcionales.

---

## Principios de trabajo

* Priorizar la calidad del diseño por sobre la rapidez de implementación.
* Evitar sobreingeniería y abstracciones prematuras.
* Mantener separación estricta entre capas.
* Modelar el dominio con lenguaje consistente con el contexto clínico.
* Favorecer soluciones simples, explícitas y fáciles de testear.
* No exponer entidades de persistencia directamente en la API.
* No introducir lógica de negocio en controllers ni en el frontend.
* Documentar y validar los contratos de API antes de implementar funcionalidades dependientes.

---

## Alcance funcional inicial

### MVP inicial

El sistema debe comenzar con un núcleo funcional mínimo, compuesto por:

* Autenticación y autorización
* Gestión de usuarios
* Gestión de roles
* Gestión de pacientes
* Gestión de médicos
* Gestión de turnos
* Dashboard inicial

### Funcionalidades diferidas

No implementar en la primera etapa, salvo que exista una necesidad explícita:

* Historias clínicas complejas
* Permisos granulares por acción
* Reportería avanzada
* Auditoría detallada
* Integraciones externas
* Notificaciones automáticas
* Múltiples sedes o multi-tenant

---

## Modelo de dominio inicial

### Entidades base

* Usuario
* Rol
* Permiso
* Paciente
* Médico
* Especialidad
* Turno

### Reglas de modelado

* Usar estados explícitos en lugar de booleanos ambiguos.
* Mantener relaciones normalizadas y consistentes.
* Evitar duplicar información derivable.
* Definir el lenguaje del dominio antes de codificar.
* No crear relaciones o campos que no respondan a una necesidad concreta del negocio.

### Estados recomendados

Usar enums o catálogos cuando corresponda para representar estados del dominio, por ejemplo:

* ACTIVO
* INACTIVO
* BLOQUEADO

---

## Contratos de API

### Enfoque

Definir primero los contratos de API mediante **OpenAPI**, antes de implementar los endpoints.

### Objetivo

* Alinear frontend y backend desde el inicio.
* Reducir retrabajo.
* Mantener documentación viva y versionable.
* Facilitar validación temprana de requests y responses.

### Contratos mínimos iniciales

* Auth: login, registro, sesión actual
* Usuarios: listado, detalle, actualización
* Roles: listado, creación, edición
* Pacientes: CRUD básico
* Médicos: CRUD básico
* Especialidades: listado y administración
* Turnos: creación, consulta, cancelación, reprogramación

### Reglas

* Toda response debe tener un esquema definido.
* Todo request debe validar estructura y campos obligatorios.
* No exponer entidades internas directamente.
* Versionar los contratos cuando el dominio evolucione.

---

## Arquitectura backend

### Stack

* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* Flyway
* OpenAPI / Swagger
* MapStruct
* Lombok cuando aporte valor
* JUnit / Spring Boot Test

### Estructura sugerida

```text
backend/
└── src/main/java/com/clinica/
    ├── config/
    ├── security/
    ├── exception/
    ├── shared/
    └── module/
        ├── auth/
        ├── usuario/
        ├── paciente/
        ├── medico/
        ├── especialidad/
        └── turno/
```

### Capas

#### Controller

* Recibe requests HTTP.
* Valida entrada básica.
* Orquesta la llamada al service.
* Devuelve DTOs, nunca entidades.

#### Service

* Contiene lógica de negocio.
* Aplica reglas del dominio.
* Gestiona transacciones.
* Coordina repositorios y mappers.

#### Repository

* Encapsula acceso a datos.
* Debe limitarse a persistencia y queries.

#### Entity

* Representa el modelo persistente.
* No debe exponerse directamente por API.

### Reglas de implementación

* Usar constructor injection.
* No acceder a repositories desde controllers.
* No poner lógica de negocio en DTOs.
* No exponer entidades en responses.
* Separar claramente request DTOs y response DTOs.
* Centralizar errores con un handler global.

---

## Seguridad y autenticación

### Estrategia

* Autenticación con JWT.
* Spring Security como capa de control de acceso.
* Contraseñas cifradas con BCrypt.
* Sesiones stateless.

### Reglas

* No almacenar contraseñas en texto plano.
* No confiar en datos no verificados del token.
* Validar expiración, firma y claims relevantes.
* Centralizar la lectura del token en un filtro dedicado.
* Definir roles iniciales simples antes de permisos granulares.

### Roles iniciales sugeridos

* ADMIN
* MEDICO
* RECEPCION

### Consideración de diseño

Los roles pueden viajar en el JWT para evitar consultas a base de datos en cada request, aceptando el trade-off de que los cambios de rol se reflejan cuando el token expira o se invalida por la estrategia definida.

---

## Arquitectura frontend

### Stack

* Angular 20
* Reactive Forms
* RxJS
* Signals para estado local
* Lazy loading por feature
* OnPush change detection
* Angular Material cuando corresponda

### Estructura sugerida

```text
frontend/src/app/
├── core/
├── shared/
├── features/
├── layout/
└── app.routes.ts
```

### Convenciones

#### core

* Servicios singleton.
* Guards.
* Interceptors.
* Autenticación y utilidades globales.

#### shared

* Componentes reutilizables.
* Pipes.
* Directivas.
* Tipos e interfaces compartidas.

#### features

* Funcionalidades por dominio.
* Rutas lazy loaded.
* Componentes y servicios específicos por módulo.

#### layout

* Shell de aplicación.
* Header.
* Sidebar.
* Footer.
* Estructura visual general.

### Reglas

* No concentrar lógica de negocio en componentes.
* Usar formularios reactivos para flujos con validación.
* No mezclar estado global con estado local sin necesidad.
* Mantener componentes pequeños y reutilizables.

---

## Persistencia y migraciones

### Base de datos

* PostgreSQL como motor principal.
* Flyway para versionar la evolución del esquema.

### Reglas

* Toda modificación estructural debe entrar por migración.
* No editar manualmente el esquema fuera del proceso de migraciones.
* Mantener nombres consistentes entre tablas, columnas y constraints.
* Preferir estructuras normalizadas antes que duplicación de datos.

### Convenciones

* Usar nombres explícitos y previsibles.
* Definir claves primarias y foráneas de forma consistente.
* Incluir timestamps de creación y actualización cuando aplique.
* Documentar decisiones de modelado que tengan impacto futuro.

---

## Orden de implementación recomendado

### Fase 1 — Definición

* Cerrar alcance del MVP.
* Definir entidades base.
* Definir reglas de negocio iniciales.
* Diseñar contratos OpenAPI.

### Fase 2 — Base técnica

* Configurar backend Spring Boot.
* Configurar frontend Angular.
* Configurar PostgreSQL.
* Configurar Flyway.
* Configurar OpenAPI.

### Fase 3 — Seguridad

* Implementar auth.
* Implementar JWT.
* Implementar Spring Security.
* Implementar login y registro.

### Fase 4 — Núcleo funcional

* Usuarios.
* Roles.
* Pacientes.
* Médicos.
* Especialidades.
* Turnos.

### Fase 5 — Calidad

* Tests unitarios.
* Tests de integración.
* Validaciones de formularios.
* Manejo global de errores.
* Revisión de consistencia arquitectónica.

### Fase 6 — Evolución

* Historias clínicas.
* Reportes.
* Auditoría.
* Notificaciones.
* Integraciones externas.

---

## Reglas de calidad

* Todo código debe ser legible y consistente.
* Toda entidad nueva debe tener una razón de existencia clara.
* Toda abstracción debe resolver un caso real.
* Toda decisión técnica importante debe quedar documentada.
* Antes de crear una solución, evaluar alternativas si el problema lo justifica.
* Si una implementación introduce deuda técnica, señalarlo explícitamente.
* No asumir requisitos no confirmados.

---

## Criterio de actuación del agente

Actuar como un arquitecto y desarrollador senior enfocado en sistemas de gestión clínica.

Antes de implementar:

* analizar el problema,
* identificar riesgos,
* proponer la opción más sólida,
* justificar decisiones relevantes.

Durante la implementación:

* priorizar claridad,
* mantener consistencia,
* evitar acoplamientos innecesarios,
* dejar el proyecto preparado para crecer por módulos.

El objetivo del proyecto es construir una base profesional desde el primer día, sin decisiones improvisadas que comprometan la evolución futura del sistema.
