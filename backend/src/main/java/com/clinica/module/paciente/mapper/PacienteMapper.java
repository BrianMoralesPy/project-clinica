package com.clinica.module.paciente.mapper;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.dto.PacienteResponse;

/**
 * Clase responsable de transformar entidades {@link Paciente}
 * en objetos {@link PacienteResponse} destinados a la capa de presentación.
 *
 * <p>
 * Centraliza la conversión entre el modelo de persistencia y el DTO
 * utilizado como respuesta de la API, evitando exponer directamente
 * las entidades JPA hacia el frontend.
 * </p>
 *
 * <p>
 * La información necesaria para construir la respuesta puede provenir
 * tanto de {@link Paciente} como de su {@code Usuario} asociado.
 * Los enums se convierten a {@code String} para facilitar su
 * representación en la respuesta de la API.
 * </p>
 *
 * <p>
 * Esta clase no contiene lógica de negocio ni mantiene estado.
 * Por ese motivo, sus métodos son estáticos y no es necesario
 * crear instancias de este mapper.
 * </p>
 */
public final class PacienteMapper {

    /**
     * Constructor privado para impedir la instanciación de la clase.
     *
     * <p>
     * {@code PacienteMapper} es una clase utilitaria compuesta
     * únicamente por métodos estáticos.
     * </p>
     */
    private PacienteMapper() {}

    /**
     * Convierte una entidad {@link Paciente} en un
     * {@link PacienteResponse}.
     *
     * <p>
     * La respuesta combina información propia del paciente con
     * datos básicos del {@code Usuario} asociado, como nombre,
     * apellido, email, username y estado.
     * </p>
     *
     * <p>
     * Los campos enumerados {@code sexo} y {@code grupoSanguineo}
     * se convierten a su representación textual mediante
     * {@link Enum#name()}. Si alguno de estos valores es {@code null},
     * se mantiene como {@code null} en la respuesta.
     * </p>
     *
     * @param entity entidad {@link Paciente} obtenida desde la capa
     *               de persistencia.
     * @return DTO {@link PacienteResponse} con la información necesaria
     *         para ser enviada al cliente.
     */
    public static PacienteResponse toResponse(Paciente entity) {

        // Construye el DTO de respuesta utilizando información
        // del paciente y de su usuario asociado.
        return new PacienteResponse(
                entity.getId(),
                entity.getUsuario().getNombre(),
                entity.getUsuario().getApellido(),
                entity.getUsuario().getEmail(),
                entity.getUsuario().getUsername(),
                entity.getDni(),
                entity.getFechaNacimiento(),
                entity.getSexo() != null ? entity.getSexo().name() : null,
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getGrupoSanguineo() != null
                        ? entity.getGrupoSanguineo().name()
                        : null,
                entity.getAlergias(),
                entity.getUsuario().getEstado().name(),
                entity.getPerfilCompleto()
        );
    }
}