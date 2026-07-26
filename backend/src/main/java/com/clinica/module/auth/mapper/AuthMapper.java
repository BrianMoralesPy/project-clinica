package com.clinica.module.auth.mapper;

import com.clinica.module.auth.dto.AuthResponse;
import com.clinica.module.usuario.entity.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthMapper {

    private AuthMapper() {}

    public static AuthResponse.UsuarioResponse toUsuarioResponse(Usuario usuario) {
        Set<String> roles = usuario.getRoles().stream()
            .map(rol -> rol.getNombre())
            .collect(Collectors.toSet());

        return new AuthResponse.UsuarioResponse(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getApellido(),
            roles,
            usuario.getCreatedAt()
        );
    }
}
