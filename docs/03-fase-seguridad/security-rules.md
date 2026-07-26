# Reglas de Seguridad — Sistema de Gestión Clínica

## Autenticación
- JWT para autenticación stateless
- BCrypt para hashing de contraseñas
- Token en header Authorization: Bearer {token}
- Expiración configurable (default 24h)

## Autorización
- Roles: ADMIN, MEDICO, RECEPCION
- Roles viajan en el JWT
- Anotación @PreAuthorize en controllers
- Endpoints públicos: /api/auth/login, /api/auth/register

## Reglas por endpoint

### Públicos
- POST /api/auth/register
- POST /api/auth/login

### Autenticados
- GET /api/auth/me

### ADMIN
- GET /api/usuarios
- GET /api/usuarios/{id}
- PUT /api/usuarios/{id}
- PATCH /api/usuarios/{id}/estado

### ADMIN, RECEPCION
- CRUD de pacientes
- CRUD de médicos
- CRUD de especialidades
- CRUD de turnos

### MEDICO
- Ver turnos propios
- Completar turnos

## Medidas de seguridad
- CSRF deshabilitado (stateless)
- Headers de seguridad via Spring Security
- CORS configurado solo para localhost:4200
- No exponer stack traces en errores
- Validación de entrada en todos los endpoints
