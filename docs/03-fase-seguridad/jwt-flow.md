# Flujo JWT — Sistema de Gestión Clínica

## Diagrama de flujo

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Frontend  │────▶│   Backend   │────▶│  Database   │
└─────────────┘     └─────────────┘     └─────────────┘
       │                   │                   │
       │  1. POST /login   │                   │
       │──────────────────▶│                   │
       │                   │  2. Buscar usuario│
       │                   │──────────────────▶│
       │                   │  3. Retornar user │
       │                   │◀──────────────────│
       │                   │                   │
       │                   │  4. Validar pass   │
       │                   │  5. Generar JWT   │
       │  6. Retornar JWT  │                   │
       │◀──────────────────│                   │
       │                   │                   │
       │  7. Request con   │                   │
       │     Bearer token  │                   │
       │──────────────────▶│                   │
       │                   │  8. JwtFilter     │
       │                   │     validar token │
       │                   │  9. Buscar user   │
       │                   │──────────────────▶│
       │                   │ 10. Retornar user │
       │                   │◀──────────────────│
       │                   │                   │
       │                   │ 11. Autorizar     │
       │  12. Response     │                   │
       │◀──────────────────│                   │
```

## Estructura del JWT

### Payload
```json
{
  "sub": "username",
  "iat": 1721913600,
  "exp": 1721999999
}
```

### Header
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

## Seguridad

- Token expira en 24 horas (configurable)
- Roles viajan en el JWT (sin consulta a DB)
- BCrypt para contraseñas
- Stateless sessions
- Filtro OncePerRequestFilter para JWT
