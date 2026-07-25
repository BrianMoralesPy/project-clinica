-- Crear buckets de Supabase Storage
INSERT INTO storage.buckets (id, name, public)
VALUES
    ('fotos-pacientes', 'fotos-pacientes', true),
    ('estudios', 'estudios', false),
    ('recetas', 'recetas', false),
    ('documentos', 'documentos', false);

-- Políticas para fotos-pacientes (público)
CREATE POLICY "Fotos accesibles públicamente"
ON storage.objects FOR SELECT
USING (bucket_id = 'fotos-pacientes');

CREATE POLICY "Solo autenticados suben fotos"
ON storage.objects FOR INSERT
WITH CHECK (bucket_id = 'fotos-pacientes' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados eliminan fotos"
ON storage.objects FOR DELETE
USING (bucket_id = 'fotos-pacientes' AND auth.role() = 'authenticated');

-- Políticas para estudios (privado)
CREATE POLICY "Solo autenticados acceden a estudios"
ON storage.objects FOR SELECT
USING (bucket_id = 'estudios' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados suben estudios"
ON storage.objects FOR INSERT
WITH CHECK (bucket_id = 'estudios' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados eliminan estudios"
ON storage.objects FOR DELETE
USING (bucket_id = 'estudios' AND auth.role() = 'authenticated');

-- Políticas para recetas (privado)
CREATE POLICY "Solo autenticados acceden a recetas"
ON storage.objects FOR SELECT
USING (bucket_id = 'recetas' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados suben recetas"
ON storage.objects FOR INSERT
WITH CHECK (bucket_id = 'recetas' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados eliminan recetas"
ON storage.objects FOR DELETE
USING (bucket_id = 'recetas' AND auth.role() = 'authenticated');

-- Políticas para documentos (privado)
CREATE POLICY "Solo autenticados acceden a documentos"
ON storage.objects FOR SELECT
USING (bucket_id = 'documentos' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados suben documentos"
ON storage.objects FOR INSERT
WITH CHECK (bucket_id = 'documentos' AND auth.role() = 'authenticated');

CREATE POLICY "Solo autenticados eliminan documentos"
ON storage.objects FOR DELETE
USING (bucket_id = 'documentos' AND auth.role() = 'authenticated');
