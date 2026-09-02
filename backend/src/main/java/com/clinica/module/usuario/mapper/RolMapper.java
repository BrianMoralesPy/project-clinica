package com.clinica.module.usuario.mapper;

import com.clinica.module.usuario.dto.RolResponse;
import com.clinica.module.usuario.entity.Rol;

/**
 * Clase responsable de transformar entidades {@link Rol}
 * en objetos {@link RolResponse} utilizados como respuesta de la API.
 *
 * <p>
 * Centraliza la conversión entre la entidad de persistencia y el DTO
 * de salida, evitando exponer directamente la entidad {@link Rol}
 * hacia las capas externas de la aplicación.
 * </p>
 *
 * <p>
 * Esta clase no contiene lógica de negocio ni mantiene estado.
 * Por ese motivo, utiliza métodos estáticos y no requiere
 * ser instanciada.
 * </p>
 */
public final class RolMapper {

    /**
     * Constructor privado para impedir la instanciación de la clase.
     *
     * <p>
     * {@code RolMapper} funciona como una clase utilitaria compuesta
     * únicamente por métodos estáticos.
     * </p>
     */
    private RolMapper() {}

    /**
     * Convierte una entidad {@link Rol} en un
     * {@link RolResponse}.
     *
     * <p>
     * La conversión toma únicamente los datos necesarios del rol
     * para construir el DTO de respuesta.
     * </p>
     *
     * @param entity entidad {@link Rol} obtenida desde la capa
     *               de persistencia.
     * @return DTO {@link RolResponse} con la información del rol
     *         que será enviada al cliente.
     */
    public static RolResponse toResponse(Rol entity) {

        // Construye y retorna el DTO de respuesta a partir
        // de los datos de la entidad persistida.
        return new RolResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }
}