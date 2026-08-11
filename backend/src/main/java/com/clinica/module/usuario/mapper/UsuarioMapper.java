package com.clinica.module.usuario.mapper;

import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.entity.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static UsuarioResponse toResponse(Usuario usuario) {
        Set<String> roles = usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toSet());

        return new UsuarioResponse(usuario.getId(),usuario.getUsername(),usuario.getEmail(),usuario.getNombre(),usuario.getApellido(),roles,
                                    usuario.getEstado().name(),usuario.getCreatedAt());
    }
}
