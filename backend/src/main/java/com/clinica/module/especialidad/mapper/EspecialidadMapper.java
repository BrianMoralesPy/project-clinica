package com.clinica.module.especialidad.mapper;

import com.clinica.module.especialidad.dto.EspecialidadResponse;
import com.clinica.module.especialidad.entity.Especialidad;

public final class EspecialidadMapper {

    private EspecialidadMapper() {}

    public static EspecialidadResponse toResponse(Especialidad entity) {
        return new EspecialidadResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }
}
