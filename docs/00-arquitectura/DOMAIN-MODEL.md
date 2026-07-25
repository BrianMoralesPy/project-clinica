# Modelo de Dominio — Sistema de Gestión Clínica

## Lenguaje del Dominio

### Entidades
- **Usuario**: Persona con acceso al sistema. Tiene credenciales y roles.
- **Rol**: Nivel de acceso (ADMIN, MEDICO, RECEPCION).
- **Permiso**: Acción específica que se puede realizar (diferido para MVP).
- **Paciente**: Persona que recibe atención médica.
- **Médico**: Profesional de salud que atiende pacientes.
- **Especialidad**: Área de conocimiento médico (Cardiología, Pediatría, etc.).
- **Turno**: Cita programada entre un paciente y un médico.
- **Archivo Adjunto**: Documento o imagen asociado a una entidad del sistema.

---

## Reglas de Negocio

### Autenticación
- Un usuario puede tener múltiples roles
- Los roles viajan en el JWT
- Las contraseñas se cifran con BCrypt
- El token expira en 24 horas (configurable)

### Pacientes
- Todo paciente debe tener DNI único
- El DNI es obligatorio y validado
- Un paciente puede tener múltiples turnos
- Un paciente puede tener múltiples archivos adjuntos

### Médicos
- Todo médico debe tener matrícula única
- Todo médico debe tener una especialidad
- Un médico puede tener múltiples turnos
- Un médico no puede tener turnos superpuestos

### Turnos
- Un turno pertenece a un paciente y un médico
- Un turno tiene un estado que sigue un ciclo:
  PROGRAMADO → CONFIRMADO → EN_ATENCION → COMPLETADO
  PROGRAMADO → CANCELADO
  PROGRAMADO → NO_ASISTIO
- Un médico no puede tener dos turnos en el mismo horario
- La duración mínima es 15 minutos, máxima 120 minutos

### Archivos
- Los archivos se asocian a entidades via polymorphic association
- Los archivos se almacenan en Supabase Storage
- Cada bucket tiene políticas de acceso específicas
- Las fotos de pacientes son públicas
- Los estudios, recetas y documentos son privados

---

## Estados del Dominio

### Usuario
| Estado | Descripción |
|--------|-------------|
| ACTIVO | Puede acceder al sistema |
| INACTIVO | Cuenta deshabilitada |
| BLOQUEADO | Bloqueado por intentos fallidos |

### Paciente
| Estado | Descripción |
|--------|-------------|
| ACTIVO | Paciente activo en el sistema |
| INACTIVO | Paciente dado de baja |

### Médico
| Estado | Descripción |
|--------|-------------|
| ACTIVO | Médico activo en el sistema |
| INACTIVO | Médico dado de baja |

### Turno
| Estado | Descripción |
|--------|-------------|
| PROGRAMADO | Turno creado, pendiente de confirmación |
| CONFIRMADO | Turno confirmado por el paciente |
| EN_ATENCION | El médico está atendiendo al paciente |
| COMPLETADO | Atención finalizada |
| CANCELADO | Turno cancelado |
| NO_ASISTIO | El paciente no se presentó |

---

## Flujo de Turnos

```
Paciente solicita turno
        ↓
Turno creado (PROGRAMADO)
        ↓
Paciente confirma (CONFIRMADO)
        ↓
Paciente llega (EN_ATENCION)
        ↓
Médico completa atención (COMPLETADO)

O en cualquier momento:
        ↓
Cancelación (CANCELADO)
o
No presentación (NO_ASISTIO)
```

---

## Flujo de Archivos

```
Usuario selecciona archivo
        ↓
Frontend envía a backend (multipart)
        ↓
Backend valida tipo y tamaño
        ↓
Backend sube a Supabase Storage
        ↓
Backend guarda registro en archivo_adjunto
        ↓
Backend retorna URL del archivo

Para descargar:
        ↓
Frontend solicita URL al backend
        ↓
Backend genera signed URL (privado) o retorna URL pública
        ↓
Frontend descarga archivo
```
