# Contratos API — Sistema de Gestión Clínica

## Convenciones
- Comunicación JSON
- Autenticación via Bearer token en header Authorization
- Paginación: query params page (0-based) y size
- Respuestas paginadas: Page<T> con content, totalElements, totalPages
- Códigos HTTP correctos (200, 201, 204, 400, 401, 403, 404, 409, 500)

---

## Auth

### POST /api/auth/register
Registrar nuevo usuario.

Request:
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "nombre": "string",
  "apellido": "string"
}
```

Response 201:
```json
{
  "token": "string",
  "tipo": "Bearer",
  "expiresIn": 86400,
  "usuario": {
    "id": 1,
    "username": "string",
    "email": "string",
    "nombre": "string",
    "apellido": "string",
    "roles": ["RECEPCION"],
    "createdAt": "2026-07-25T10:00:00Z"
  }
}
```

### POST /api/auth/login
Iniciar sesión.

Request:
```json
{
  "username": "string",
  "password": "string"
}
```

Response 200: (misma estructura que register)

### GET /api/auth/me
Obtener usuario autenticado actual.

Headers: Authorization: Bearer {token}

Response 200:
```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "nombre": "string",
  "apellido": "string",
  "roles": ["ADMIN"],
  "createdAt": "2026-07-25T10:00:00Z"
}
```

---

## Usuarios

### GET /api/usuarios
Listar usuarios (paginado). Acceso: ADMIN

Query params: page, size, search

Response 200:
```json
{
  "content": [
    {
      "id": 1,
      "username": "string",
      "email": "string",
      "nombre": "string",
      "apellido": "string",
      "roles": ["ADMIN"],
      "estado": "ACTIVO",
      "createdAt": "2026-07-25T10:00:00Z"
    }
  ],
  "totalElements": 10,
  "totalPages": 1
}
```

### GET /api/usuarios/{id}
Obtener usuario por ID. Acceso: ADMIN

Response 200: (objeto UsuarioResponse)

### PUT /api/usuarios/{id}
Actualizar usuario. Acceso: ADMIN

Request:
```json
{
  "email": "string",
  "nombre": "string",
  "apellido": "string"
}
```

Response 200: (UsuarioResponse)

### PATCH /api/usuarios/{id}/estado
Cambiar estado de usuario. Acceso: ADMIN

Request:
```json
{
  "estado": "ACTIVO | INACTIVO | BLOQUEADO"
}
```

Response 200: (UsuarioResponse)

---

## Roles

### GET /api/roles
Listar todos los roles. Acceso: ADMIN

Response 200:
```json
[
  {
    "id": 1,
    "nombre": "ADMIN",
    "descripcion": "Administrador del sistema"
  }
]
```

### POST /api/roles
Crear rol. Acceso: ADMIN

Request:
```json
{
  "nombre": "string",
  "descripcion": "string"
}
```

Response 201: (RolResponse)

### PUT /api/roles/{id}
Actualizar rol. Acceso: ADMIN

Request:
```json
{
  "nombre": "string",
  "descripcion": "string"
}
```

Response 200: (RolResponse)

---

## Pacientes

### GET /api/pacientes
Listar pacientes (paginado). Acceso: ADMIN, RECEPCION

Query params: page, size, search, estado

Response 200: Page<PacienteResponse>

### GET /api/pacientes/{id}
Obtener paciente. Acceso: ADMIN, RECEPCION, MEDICO

Response 200:
```json
{
  "id": 1,
  "nombre": "string",
  "apellido": "string",
  "dni": "string",
  "fechaNacimiento": "1990-01-15",
  "sexo": "MASCULINO",
  "telefono": "string",
  "email": "string",
  "direccion": "string",
  "grupoSanguineo": "O_POSITIVO",
  "alergias": "string",
  "estado": "ACTIVO",
  "createdAt": "2026-07-25T10:00:00Z"
}
```

### POST /api/pacientes
Crear paciente. Acceso: ADMIN, RECEPCION

Request:
```json
{
  "nombre": "string",
  "apellido": "string",
  "dni": "string",
  "fechaNacimiento": "1990-01-15",
  "sexo": "MASCULINO",
  "telefono": "string",
  "email": "string",
  "direccion": "string",
  "grupoSanguineo": "O_POSITIVO",
  "alergias": "string"
}
```

Response 201: (PacienteResponse)

### PUT /api/pacientes/{id}
Actualizar paciente. Acceso: ADMIN, RECEPCION

Request: (misma estructura que POST)

Response 200: (PacienteResponse)

### DELETE /api/pacientes/{id}
Eliminar paciente. Acceso: ADMIN

Response 204

---

## Médicos

### GET /api/medicos
Listar médicos (paginado). Acceso: ADMIN, RECEPCION

Query params: page, size, especialidadId, estado

Response 200: Page<MedicoResponse>

### GET /api/medicos/{id}
Obtener médico. Acceso: ADMIN, RECEPCION

Response 200:
```json
{
  "id": 1,
  "nombre": "string",
  "apellido": "string",
  "dni": "string",
  "matricula": "string",
  "especialidad": {
    "id": 1,
    "nombre": "string"
  },
  "telefono": "string",
  "email": "string",
  "estado": "ACTIVO",
  "createdAt": "2026-07-25T10:00:00Z"
}
```

### POST /api/medicos
Crear médico. Acceso: ADMIN

Request:
```json
{
  "nombre": "string",
  "apellido": "string",
  "dni": "string",
  "matricula": "string",
  "especialidadId": 1,
  "telefono": "string",
  "email": "string"
}
```

Response 201: (MedicoResponse)

### PUT /api/medicos/{id}
Actualizar médico. Acceso: ADMIN

Request: (misma estructura que POST)

Response 200: (MedicoResponse)

### DELETE /api/medicos/{id}
Eliminar médico. Acceso: ADMIN

Response 204

---

## Especialidades

### GET /api/especialidades
Listar todas las especialidades. Acceso: ADMIN, RECEPCION

Response 200:
```json
[
  {
    "id": 1,
    "nombre": "Cardiología",
    "descripcion": "Especialidad del corazón"
  }
]
```

### POST /api/especialidades
Crear especialidad. Acceso: ADMIN

Request:
```json
{
  "nombre": "string",
  "descripcion": "string"
}
```

Response 201: (EspecialidadResponse)

### PUT /api/especialidades/{id}
Actualizar especialidad. Acceso: ADMIN

Request: (misma estructura que POST)

Response 200: (EspecialidadResponse)

---

## Turnos

### GET /api/turnos
Listar turnos (paginado). Acceso: ADMIN, RECEPCION, MEDICO

Query params: page, size, fechaDesde, fechaHasta, medicoId, pacienteId, estado

Response 200: Page<TurnoResponse>

### GET /api/turnos/{id}
Obtener turno. Acceso: ADMIN, RECEPCION, MEDICO

Response 200:
```json
{
  "id": 1,
  "paciente": {
    "id": 1,
    "nombre": "string",
    "apellido": "string"
  },
  "medico": {
    "id": 1,
    "nombre": "string",
    "apellido": "string"
  },
  "fechaHora": "2026-07-25T10:00:00Z",
  "duracionMinutos": 30,
  "motivo": "string",
  "observaciones": "string",
  "estado": "PROGRAMADO",
  "createdAt": "2026-07-25T10:00:00Z"
}
```

### POST /api/turnos
Crear turno. Acceso: ADMIN, RECEPCION

Request:
```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "fechaHora": "2026-07-25T10:00:00Z",
  "duracionMinutos": 30,
  "motivo": "string"
}
```

Response 201: (TurnoResponse)

### PUT /api/turnos/{id}
Reprogramar turno. Acceso: ADMIN, RECEPCION

Request:
```json
{
  "fechaHora": "2026-07-26T14:00:00Z",
  "duracionMinutos": 30
}
```

Response 200: (TurnoResponse)

### PATCH /api/turnos/{id}/cancelar
Cancelar turno. Acceso: ADMIN, RECEPCION

Response 200: (TurnoResponse con estado CANCELADO)

### PATCH /api/turnos/{id}/completar
Completar turno. Acceso: MEDICO

Request:
```json
{
  "observaciones": "string"
}
```

Response 200: (TurnoResponse con estado COMPLETADO)

---

## Archivos

### POST /api/archivos
Subir archivo (multipart/form-data). Acceso: ADMIN, RECEPCION, MEDICO

Form fields:
- file: archivo
- entidadTipo: PACIENTE | TURNO | MEDICO
- entidadId: number
- bucket: fotos-pacientes | estudios | recetas | documentos

Response 201:
```json
{
  "id": 1,
  "entidadTipo": "PACIENTE",
  "entidadId": 1,
  "bucket": "fotos-pacientes",
  "rutaArchivo": "1/foto.jpg",
  "nombreOriginal": "foto.jpg",
  "tipoMimeType": "image/jpeg",
  "tamanoBytes": 102400,
  "url": "https://...supabase.co/storage/v1/object/public/fotos-pacientes/1/foto.jpg",
  "createdAt": "2026-07-25T10:00:00Z"
}
```

### GET /api/archivos/{id}/url
Obtener URL del archivo. Acceso: AUTENTICADO

Response 200:
```json
{
  "url": "https://...supabase.co/storage/v1/..."
}
```

### GET /api/archivos
Listar archivos de una entidad. Acceso: AUTENTICADO

Query params: entidadTipo, entidadId

Response 200: List<ArchivoAdjuntoResponse>

### DELETE /api/archivos/{id}
Eliminar archivo. Acceso: ADMIN

Response 204
