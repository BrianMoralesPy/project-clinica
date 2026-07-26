package com.clinica.module.medico.mapper;

import com.clinica.module.medico.dto.EspecialidadResumen;
import com.clinica.module.medico.dto.MedicoResponse;
import com.clinica.module.medico.entity.Medico;

import java.time.ZoneOffset;

public final class MedicoMapper {

    private MedicoMapper() {}

    public static MedicoResponse toResponse(Medico entity) {
        return new MedicoResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getDni(),
            entity.getMatricula(),
            new EspecialidadResumen(
                entity.getEspecialidad().getId(),
                entity.getEspecialidad().getNombre()
            ),
            entity.getTelefono(),
            entity.getEmail(),
            entity.getEstado().name(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(ZoneOffset.UTC).toInstant() : null
        );
    }
}
