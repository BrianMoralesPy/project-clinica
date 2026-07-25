CREATE TABLE medico (
    id               BIGSERIAL PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    apellido         VARCHAR(100) NOT NULL,
    dni              VARCHAR(20)  NOT NULL UNIQUE,
    matricula        VARCHAR(50)  NOT NULL UNIQUE,
    especialidad_id  BIGINT       NOT NULL REFERENCES especialidad(id),
    telefono         VARCHAR(30),
    email            VARCHAR(100),
    estado           VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO',
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_medico_matricula ON medico(matricula);
CREATE INDEX idx_medico_especialidad ON medico(especialidad_id);
