package com.clinica.module.usuario.mapper;

import com.clinica.module.usuario.dto.RolResponse;
import com.clinica.module.usuario.entity.Rol;

public final class RolMapper {

    private RolMapper() {}

    public static RolResponse toResponse(Rol entity) {
        return new RolResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }
}
