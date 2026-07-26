package com.clinica.module.archivo.mapper;

import com.clinica.module.archivo.dto.ArchivoAdjuntoResponse;
import com.clinica.module.archivo.entity.ArchivoAdjunto;

import java.time.ZoneOffset;

public final class ArchivoMapper {

    private ArchivoMapper() {}

    public static ArchivoAdjuntoResponse toResponse(ArchivoAdjunto entity, String url) {
        return new ArchivoAdjuntoResponse(
            entity.getId(),
            entity.getEntidadTipo().name(),
            entity.getEntidadId(),
            entity.getBucket(),
            entity.getRutaArchivo(),
            entity.getNombreOriginal(),
            entity.getTipoMime(),
            entity.getTamanoBytes(),
            url,
            entity.getCreatedAt() != null ? entity.getCreatedAt() : null
        );
    }
}
