package com.clinica.module.usuario.mapper;

import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.entity.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase responsable de transformar entidades {@link Usuario}
 * en objetos {@link UsuarioResponse} utilizados como respuesta de la API.
 *
 * <p>
 * Centraliza la conversión entre el modelo de persistencia y el DTO
 * de salida, evitando exponer directamente la entidad {@link Usuario}
 * hacia las capas externas de la aplicación.
 * </p>
 *
 * <p>
 * Además de los datos propios del usuario, el mapper transforma la
 * colección de roles asociados en un {@link Set} de nombres de roles,
 * proporcionando al cliente únicamente la información necesaria.
 * </p>
 *
 * <p>
 * Esta clase no contiene lógica de negocio ni mantiene estado.
 * Sus métodos son estáticos y la clase no debe ser instanciada.
 * </p>
 */
public class UsuarioMapper {

    /**
     * Constructor privado para impedir la instanciación de la clase.
     *
     * <p>
     * El mapper funciona como una clase utilitaria compuesta
     * únicamente por métodos estáticos.
     * </p>
     */
    private UsuarioMapper() {}

    /**
     * Convierte una entidad {@link Usuario} en un
     * {@link UsuarioResponse}.
     *
     * <p>
     * Los roles asociados al usuario se transforman desde objetos
     * {@code Rol} a un conjunto de nombres de roles ({@link String}),
     * evitando exponer las entidades de persistencia en la respuesta.
     * </p>
     *
     * <p>
     * El estado del usuario se convierte a su representación textual
     * mediante {@link Enum#name()}.
     * </p>
     *
     * @param usuario entidad {@link Usuario} obtenida desde la capa
     *                de persistencia.
     * @return DTO {@link UsuarioResponse} con la información necesaria
     *         para ser enviada al cliente.
     */
    public static UsuarioResponse toResponse(Usuario usuario) {

        // Obtiene únicamente el nombre de cada rol asociado
        // para incluirlo en el DTO de respuesta.
        Set<String> roles = usuario.getRoles()
                .stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toSet());

        // Construye y retorna el DTO con los datos públicos
        // necesarios del usuario.
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                roles,
                usuario.getEstado().name(),
                usuario.getCreatedAt()
        );
    }
}
