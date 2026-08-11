package com.clinica.module.auth.mapper;

import com.clinica.module.auth.dto.AuthResponse;
import com.clinica.module.usuario.entity.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper encargado de convertir entidades Usuario
 * en los DTO utilizados por el módulo de autenticación.
 *
 * Centralizar estas conversiones evita duplicar código
 * en los servicios y mantiene separadas las responsabilidades.
 */
public class AuthMapper {
    
    /**
     * Evita que esta clase pueda ser instanciada.
     * Tod0s sus métodos son estáticos.
     */
    private AuthMapper() {}
    
    /**
     * Convierte una entidad Usuario en un UsuarioResponse.
     *
     * Solo expone la información necesaria para el frontend,
     * evitando enviar datos sensibles como la contraseña.
     *
     * @param usuario Entidad obtenida desde la base de datos.
     * @return DTO con la información pública del usuario.
     */
    public static AuthResponse.UsuarioResponse toUsuarioResponse(Usuario usuario) {
        // Convierte la colección de roles del usuario en un conjunto que contiene únicamente sus nombres.
        Set<String> roles = usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toSet());
        // Construye el DTO que será enviado al frontend retornandolo.
        return new AuthResponse.UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getEmail(),usuario.getNombre(),
                                                usuario.getApellido(),roles,usuario.getCreatedAt());
    }


}
