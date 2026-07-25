CREATE TABLE archivo_adjunto (
    id              BIGSERIAL PRIMARY KEY,
    entidad_tipo    VARCHAR(30)  NOT NULL,
    entidad_id      BIGINT       NOT NULL,
    bucket          VARCHAR(50)  NOT NULL,
    ruta_archivo    VARCHAR(500) NOT NULL,
    nombre_original VARCHAR(200) NOT NULL,
    tipo_mime       VARCHAR(100) NOT NULL,
    tamano_bytes    BIGINT       NOT NULL,
    subido_por      BIGINT       REFERENCES usuario(id),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_archivo_entidad ON archivo_adjunto(entidad_tipo, entidad_id);
