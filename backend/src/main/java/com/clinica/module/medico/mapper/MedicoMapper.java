package com.clinica.module.medico.mapper;

import com.clinica.module.medico.dto.EspecialidadResumen;
import com.clinica.module.medico.dto.MedicoResponse;
import com.clinica.module.medico.entity.Medico;

/**
 * Clase encargada de transformar entidades {@link Medico} en objetos de respuesta
 * {@link MedicoResponse}.
 *
 * <p>
 * Centraliza la conversión entre la capa de persistencia y la capa de presentación,
 * evitando exponer directamente la entidad {@link Medico} hacia los controladores
 * o clientes de la API.
 * </p>
 *
 * <p>
 * Esta clase no posee estado ni lógica de negocio, por lo que sus métodos son
 * estáticos y no requiere ser instanciada.
 * </p>
 */
public final class MedicoMapper {

    /**
     * Constructor privado para evitar la instanciación de la clase.
     *
     * <p>
     * {@code MedicoMapper} funciona como una clase utilitaria de conversión,
     * por lo que no debe crearse mediante {@code new}.
     * </p>
     */
    private MedicoMapper() {}

    /**
     * Convierte una entidad {@link Medico} en su correspondiente
     * {@link MedicoResponse}.
     *
     * <p>
     * La información se obtiene tanto de la entidad médica como de su
     * {@link com.clinica.module.usuario.entity.Usuario} asociado y de la
     * especialidad correspondiente.
     * </p>
     *
     * @param entity entidad {@link Medico} que se desea transformar
     * @return objeto {@link MedicoResponse} con la información necesaria
     *         para exponer en la respuesta de la API
     */
    public static MedicoResponse toResponse(Medico entity) {
        return new MedicoResponse(
                entity.getId(),
                entity.getUsuario().getNombre(),
                entity.getUsuario().getApellido(),
                entity.getUsuario().getEmail(),
                entity.getDni(),
                new EspecialidadResumen(
                        entity.getEspecialidad().getId(),
                        entity.getEspecialidad().getNombre()
                ),
                entity.getMatricula(),
                entity.getFechaNacimiento(),
                entity.getTelefono(),
                entity.getUsuario().getEstado().name()
        );
    }
}
