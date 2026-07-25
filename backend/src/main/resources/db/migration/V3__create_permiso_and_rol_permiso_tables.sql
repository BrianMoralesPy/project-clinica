CREATE TABLE permiso (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE rol_permiso (
    rol_id     BIGINT NOT NULL REFERENCES rol(id) ON DELETE CASCADE,
    permiso_id BIGINT NOT NULL REFERENCES permiso(id) ON DELETE CASCADE,
    PRIMARY KEY (rol_id, permiso_id)
);
