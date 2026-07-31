INSERT INTO usuario (
    username,
    email,
    password_hash,
    nombre,
    apellido,
    estado
)
VALUES (
    'admin',
    'admin@clinica.com',
    '$2a$10$jCpySHDFtNxz7suVOT1plO.2UC8sOSymiwKWtCXVYmsTN9g6G20Hy',
    'Brian',
    'Morales',
    'ACTIVO'
);

INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT
    u.id,
    r.id
FROM usuario u
JOIN rol r ON r.nombre = 'ADMIN'
WHERE u.username = 'admin';