CREATE TABLE turno (
    id                BIGSERIAL PRIMARY KEY,
    paciente_id       BIGINT       NOT NULL REFERENCES paciente(id),
    medico_id         BIGINT       NOT NULL REFERENCES medico(id),
    fecha_hora        TIMESTAMPTZ  NOT NULL,
    duracion_minutos  INT          NOT NULL DEFAULT 30,
    motivo            VARCHAR(300),
    observaciones     TEXT,
    estado            VARCHAR(20)  NOT NULL DEFAULT 'PROGRAMADO',
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_turno_fecha ON turno(fecha_hora);
CREATE INDEX idx_turno_paciente ON turno(paciente_id);
CREATE INDEX idx_turno_medico ON turno(medico_id);
