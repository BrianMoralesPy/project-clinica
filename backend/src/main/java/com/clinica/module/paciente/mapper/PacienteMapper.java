package com.clinica.module.paciente.mapper;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.dto.PacienteResponse;

import java.time.ZoneOffset;

public final class PacienteMapper {

    private PacienteMapper() {}

    public static PacienteResponse toResponse(Paciente entity) {
        return new PacienteResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getDni(),
            entity.getFechaNacimiento(),
            entity.getSexo().name(),
            entity.getTelefono(),
            entity.getEmail(),
            entity.getDireccion(),
            entity.getGrupoSanguineo() != null ? entity.getGrupoSanguineo().name() : null,
            entity.getAlergias(),
            entity.getEstado().name(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(ZoneOffset.UTC).toInstant() : null
        );
    }
}
