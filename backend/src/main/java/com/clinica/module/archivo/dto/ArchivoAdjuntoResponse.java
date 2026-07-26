package com.clinica.module.archivo.dto;

import java.time.Instant;

public record ArchivoAdjuntoResponse(
    Long id,
    String entidadTipo,
    Long entidadId,
    String bucket,
    String rutaArchivo,
    String nombreOriginal,
    String tipoMime,
    Long tamanoBytes,
    String url,
    Instant createdAt
) {}
