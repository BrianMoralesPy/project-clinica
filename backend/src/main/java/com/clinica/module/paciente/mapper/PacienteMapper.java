package com.clinica.module.paciente.mapper;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.dto.PacienteResponse;


public final class PacienteMapper {

    private PacienteMapper() {}
    /**
     * Convierte una entidad Paciente en un PacienteResponse.
     *
     * Solo expone la información necesaria para el frontend,
     * evitando enviar datos sensibles.
     *
     * @param paciente Entidad obtenida desde la base de datos.
     * @return DTO con la información pública del usuario.
     */
    public static PacienteResponse toResponse(Paciente entity) {
        // Construye el DTO que será enviado al frontend retornandolo.
        return new PacienteResponse(entity.getId(),entity.getUsuario().getNombre(),entity.getUsuario().getApellido(),entity.getUsuario().getEmail(),
                                    entity.getDni(),entity.getFechaNacimiento(),entity.getSexo() != null ? entity.getSexo().name(): null,
                                    entity.getTelefono(),entity.getDireccion(),entity.getGrupoSanguineo() != null ? entity.getGrupoSanguineo().name() : null,
                                    entity.getAlergias(),entity.getUsuario().getEstado().name(),entity.getPerfilCompleto());
    }
}
