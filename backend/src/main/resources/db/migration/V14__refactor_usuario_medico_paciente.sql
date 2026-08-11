-- =====================================================
-- PACIENTE
-- =====================================================

ALTER TABLE paciente
    DROP COLUMN IF EXISTS nombre,
    DROP COLUMN IF EXISTS apellido,
    DROP COLUMN IF EXISTS email,
    DROP COLUMN IF EXISTS estado;

ALTER TABLE paciente
    ALTER COLUMN id DROP DEFAULT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_paciente_usuario'
    ) THEN

        ALTER TABLE paciente
        ADD CONSTRAINT fk_paciente_usuario
        FOREIGN KEY (id)
        REFERENCES usuario(id)
        ON DELETE CASCADE;

    END IF;
END $$;


-- =====================================================
-- MEDICO
-- =====================================================

ALTER TABLE medico
    DROP COLUMN IF EXISTS nombre,
    DROP COLUMN IF EXISTS apellido,
    DROP COLUMN IF EXISTS email,
    DROP COLUMN IF EXISTS estado;

ALTER TABLE medico
    ADD COLUMN IF NOT EXISTS fecha_nacimiento DATE;

ALTER TABLE medico
    ALTER COLUMN id DROP DEFAULT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_medico_usuario'
    ) THEN

        ALTER TABLE medico
        ADD CONSTRAINT fk_medico_usuario
        FOREIGN KEY (id)
        REFERENCES usuario(id)
        ON DELETE CASCADE;

    END IF;
END $$;