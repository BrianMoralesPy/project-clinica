CREATE TABLE rol (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE usuario_rol (
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    rol_id     BIGINT NOT NULL REFERENCES rol(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, rol_id)
);
