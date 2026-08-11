
-- =====================================================
-- PACIENTE
-- =====================================================
ALTER TABLE paciente
    ALTER COLUMN dni DROP NOT NULL;

ALTER TABLE paciente
    ALTER COLUMN fecha_nacimiento DROP NOT NULL;

ALTER TABLE paciente
    ALTER COLUMN sexo DROP NOT NULL;

ALTER TABLE paciente
    ADD COLUMN perfil_completo BOOLEAN NOT NULL DEFAULT FALSE;

