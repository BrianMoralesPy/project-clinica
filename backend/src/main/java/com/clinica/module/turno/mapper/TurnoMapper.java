package com.clinica.module.turno.mapper;

import com.clinica.module.turno.dto.ResumenMedico;
import com.clinica.module.turno.dto.ResumenPaciente;
import com.clinica.module.turno.dto.TurnoResponse;
import com.clinica.module.turno.entity.Turno;

import java.time.ZoneOffset;

public final class TurnoMapper {

    private TurnoMapper() {}

    public static TurnoResponse toResponse(Turno entity) {
        return new TurnoResponse(
            entity.getId(),
            new ResumenPaciente(
                entity.getPaciente().getId(),
                entity.getPaciente().getNombre(),
                entity.getPaciente().getApellido()
            ),
            new ResumenMedico(
                entity.getMedico().getId(),
                entity.getMedico().getNombre(),
                entity.getMedico().getApellido(),
                entity.getMedico().getEspecialidad().getNombre()
            ),
            entity.getFechaHora(),
            entity.getDuracionMinutos(),
            entity.getMotivo(),
            entity.getObservaciones(),
            entity.getEstado().name(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().atZone(ZoneOffset.UTC).toInstant() : null
        );
    }
}
