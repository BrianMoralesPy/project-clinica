# Fase 3 — Seguridad

## Objetivo
Implementar autenticación JWT, autorización por roles, login/registro y layout base de la aplicación.

## Estado: COMPLETADA

## Archivos creados

### Backend
- `shared/BaseEntity.java` — Clase base con timestamps
- `shared/Estado*.java` — Enums del dominio
- `module/usuario/entity/Usuario.java` — Entidad Usuario
- `module/usuario/entity/Rol.java` — Entidad Rol
- `module/usuario/entity/Permiso.java` — Entidad Permiso
- `module/usuario/repository/UsuarioRepository.java` — Repositorio con búsquedas
- `module/usuario/repository/RolRepository.java` — Repositorio de roles
- `module/usuario/service/UsuarioService.java` — Servicio CRUD
- `module/usuario/controller/UsuarioController.java` — API REST
- `security/JwtTokenProvider.java` — Generación y validación de JWT
- `security/JwtAuthenticationFilter.java` — Filtro de autenticación
- `security/UserDetailsServiceImpl.java` — Carga de usuarios
- `config/SecurityConfiguration.java` — Configuración de seguridad
- `config/CorsConfiguration.java` — Configuración CORS
- `config/OpenApiConfiguration.java` — Configuración Swagger
- `exception/GlobalExceptionHandler.java` — Manejo global de errores
- `exception/ResourceNotFoundException.java` — Excepción 404
- `exception/BadRequestException.java` — Excepción 400
- `module/auth/dto/*.java` — DTOs de autenticación
- `module/auth/mapper/AuthMapper.java` — Mapper de auth
- `module/auth/service/AuthService.java` — Servicio de auth
- `module/auth/controller/AuthController.java` — API de auth

### Frontend
- `core/auth/auth.model.ts` — Modelos de auth
- `core/auth/auth.service.ts` — Servicio de auth con signals
- `core/auth/auth.guard.ts` — Guards de autenticación
- `core/auth/auth.interceptor.ts` — Interceptor JWT
- `features/auth/pages/login-page/` — Página de login
- `features/auth/pages/register-page/` — Página de registro
- `layout/components/sidebar/` — Sidebar de navegación
- `layout/components/header/` — Header con usuario
- `layout/layout.component.ts` — Layout principal
- `features/dashboard/pages/dashboard.component.ts` — Dashboard placeholder
- `features/*/pages/*-list.component.ts` — Placeholders para módulos

## Endpoints implementados

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | /api/auth/register | Registro | Público |
| POST | /api/auth/login | Login | Público |
| GET | /api/auth/me | Usuario actual | Autenticado |
| GET | /api/usuarios | Listar usuarios | ADMIN |
| GET | /api/usuarios/{id} | Obtener usuario | ADMIN |
| PUT | /api/usuarios/{id} | Actualizar usuario | ADMIN |
| PATCH | /api/usuarios/{id}/estado | Cambiar estado | ADMIN |

## Roles iniciales
- ADMIN
- MEDICO
- RECEPCION

## Próximos pasos
- Fase 4: Núcleo funcional (CRUD de módulos)
