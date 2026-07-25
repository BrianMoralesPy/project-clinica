CREATE TABLE paciente (
    id                BIGSERIAL PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL,
    apellido          VARCHAR(100) NOT NULL,
    dni               VARCHAR(20)  NOT NULL UNIQUE,
    fecha_nacimiento  DATE         NOT NULL,
    sexo              VARCHAR(20)  NOT NULL,
    telefono          VARCHAR(30),
    email             VARCHAR(100),
    direccion         VARCHAR(200),
    grupo_sanguineo   VARCHAR(10),
    alergias          TEXT,
    estado            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO',
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_paciente_dni ON paciente(dni);
