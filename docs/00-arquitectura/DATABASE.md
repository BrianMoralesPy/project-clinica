# Base de Datos — Sistema de Gestión Clínica

## Motor
PostgreSQL alojado en Supabase.

## Convenciones
- Tablas en snake_case
- Claves primarias: BIGSERIAL
- Timestamps: TIMESTAMPTZ con DEFAULT NOW()
- Enums como VARCHAR con CHECK o valores predefinidos
- Foreign keys con ON DELETE apropiado

---

## Esquema Completo

### usuario
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| username | VARCHAR(50) | NOT NULL UNIQUE |
| email | VARCHAR(100) | NOT NULL UNIQUE |
| password_hash | VARCHAR(255) | NOT NULL |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| estado | VARCHAR(20) | NOT NULL DEFAULT 'ACTIVO' |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |
| updated_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

Estados: ACTIVO, INACTIVO, BLOQUEADO

### rol
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(50) | NOT NULL UNIQUE |
| descripcion | VARCHAR(200) | |

### usuario_rol
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| usuario_id | BIGINT | FK → usuario(id) ON DELETE CASCADE |
| rol_id | BIGINT | FK → rol(id) ON DELETE CASCADE |

PK compuesta: (usuario_id, rol_id)

### permiso
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL UNIQUE |
| descripcion | VARCHAR(200) | |

### rol_permiso
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| rol_id | BIGINT | FK → rol(id) ON DELETE CASCADE |
| permiso_id | BIGINT | FK → permiso(id) ON DELETE CASCADE |

PK compuesta: (rol_id, permiso_id)

### especialidad
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL UNIQUE |
| descripcion | VARCHAR(300) | |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |
| updated_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

### paciente
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| dni | VARCHAR(20) | NOT NULL UNIQUE |
| fecha_nacimiento | DATE | NOT NULL |
| sexo | VARCHAR(20) | NOT NULL |
| telefono | VARCHAR(30) | |
| email | VARCHAR(100) | |
| direccion | VARCHAR(200) | |
| grupo_sanguineo | VARCHAR(10) | |
| alergias | TEXT | |
| estado | VARCHAR(20) | NOT NULL DEFAULT 'ACTIVO' |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |
| updated_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

Estados: ACTIVO, INACTIVO
Sexo: MASCULINO, FEMENINO, OTRO
Grupo Sanguíneo: A_POSITIVO, A_NEGATIVO, B_POSITIVO, B_NEGATIVO, AB_POSITIVO, AB_NEGATIVO, O_POSITIVO, O_NEGATIVO

### medico
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| nombre | VARCHAR(100) | NOT NULL |
| apellido | VARCHAR(100) | NOT NULL |
| dni | VARCHAR(20) | NOT NULL UNIQUE |
| matricula | VARCHAR(50) | NOT NULL UNIQUE |
| especialidad_id | BIGINT | FK → especialidad(id) |
| telefono | VARCHAR(30) | |
| email | VARCHAR(100) | |
| estado | VARCHAR(20) | NOT NULL DEFAULT 'ACTIVO' |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |
| updated_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

Estados: ACTIVO, INACTIVO

### turno
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| paciente_id | BIGINT | FK → paciente(id) |
| medico_id | BIGINT | FK → medico(id) |
| fecha_hora | TIMESTAMPTZ | NOT NULL |
| duracion_minutos | INT | NOT NULL DEFAULT 30 |
| motivo | VARCHAR(300) | |
| observaciones | TEXT | |
| estado | VARCHAR(20) | NOT NULL DEFAULT 'PROGRAMADO' |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |
| updated_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

Estados: PROGRAMADO, CONFIRMADO, EN_ATENCION, COMPLETADO, CANCELADO, NO_ASISTIO

### archivo_adjunto
| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGSERIAL | PK |
| entidad_tipo | VARCHAR(30) | NOT NULL |
| entidad_id | BIGINT | NOT NULL |
| bucket | VARCHAR(50) | NOT NULL |
| ruta_archivo | VARCHAR(500) | NOT NULL |
| nombre_original | VARCHAR(200) | NOT NULL |
| tipo_mime | VARCHAR(100) | NOT NULL |
| tamano_bytes | BIGINT | NOT NULL |
| subido_por | BIGINT | FK → usuario(id) |
| created_at | TIMESTAMPTZ | NOT NULL DEFAULT NOW() |

Entidad Tipo: PACIENTE, TURNO, MEDICO

---

## Índices

```sql
CREATE INDEX idx_paciente_dni ON paciente(dni);
CREATE INDEX idx_medico_matricula ON medico(matricula);
CREATE INDEX idx_medico_especialidad ON medico(especialidad_id);
CREATE INDEX idx_turno_fecha ON turno(fecha_hora);
CREATE INDEX idx_turno_paciente ON turno(paciente_id);
CREATE INDEX idx_turno_medico ON turno(medico_id);
CREATE INDEX idx_archivo_entidad ON archivo_adjunto(entidad_tipo, entidad_id);
```

---

## Migraciones Flyway

| Archivo | Contenido |
|---------|-----------|
| V1 | Tabla usuario |
| V2 | Tablas rol y usuario_rol |
| V3 | Tablas permiso y rol_permiso |
| V4 | Tabla especialidad |
| V5 | Tabla paciente |
| V6 | Tabla medico |
| V7 | Tabla turno |
| V8 | Roles iniciales (ADMIN, MEDICO, RECEPCION) |
| V9 | Especialidades iniciales |
| V10 | Tabla archivo_adjunto |
| V11 | Buckets de Storage y políticas |

---

## Supabase Storage

### Buckets
| Bucket | Público | Contenido |
|--------|---------|-----------|
| fotos-pacientes | Sí | Fotos de perfil de pacientes |
| estudios | No | Análisis, imágenes médicas |
| recetas | No | Recetas médicas |
| documentos | No | Documentos generales |

### Estructura de archivos
```
fotos-pacientes/{paciente_id}/foto.jpg
estudios/{paciente_id}/{fecha}_{descripcion}.pdf
recetas/{paciente_id}/{fecha}_{medicamento}.pdf
documentos/{paciente_id}/{descripcion}.pdf
```
